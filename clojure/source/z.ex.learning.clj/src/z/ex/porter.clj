(ns z.ex.porter)
; This code is from the Eric Rochester blogs:
; http://writingcoding.blogspot.com/2008/07/stemming-part-1-introduction.html
; It's based on the C version from http://tartarus.org/~martin/PorterStemmer/index.html
; whose license says "...the notes about licensing are never more restrictive
; than the BSD License"

(defrecord stemmer [word index])

(defn make-stemmer
  "This returns a stemmer structure for a given word."
  [word]
  (stemmer. (vec word) (dec (count word))))

(defn reset-index
  "this returns a new stemmer with the :word vector and :index set to 
the last index."
  [word-vec]
  (stemmer. word-vec (dec (count word-vec))))

(defn get-index
  "This returns a valid value of j."
  [stemmer]
  (if-let [j (:index stemmer)]
    (min j (dec (count (:word stemmer))))
    (dec (count (:word stemmer)))))


(defn subword
  "This returns the subword in the stemmer from 0..j."
  [stemmer]
  (let [b (:word stemmer), j (inc (get-index stemmer))]
    (if (< j (count b))
    (subvec b 0 j)
    b)))

(defn index-char
  "This returns the index-char character in the word."
  [stemmer]
  (nth (:word stemmer) (get-index stemmer)))

(defn pop-word
  "This returns the stemmer with one char popped of the list."
  [stemmer]
  (assoc stemmer :word (pop (:word stemmer))))

(defn pop-stemmer-on
  "This is an amalgam of a number of
  different functions: pop (it walks
  through the :word sequence using pop);
  drop-while (it drops items off while
  testing the sequence against drop-while);
  and maplist from Common Lisp (the
  predicate is tested against the entire
  current stemmer, not just the first
  element)."
  [predicate stemmer]
  (if (and (seq (:word stemmer)) (predicate stemmer))
    (recur predicate (pop-word stemmer))
    stemmer))

(def vowel-letter? #{\a \e \i \o \u})


(defn consonant?
  "Returns true if the ith character in a stemmer
  is a consonant. i defaults to :index."
  ([stemmer]
    (consonant? stemmer (get-index stemmer)))
  ([stemmer i]
    (let [c (nth (:word stemmer) i)]
      (cond (vowel-letter? c) false
            (= c \y) (if (zero? i)
                       true
                       (not (consonant? stemmer (dec i))))
            :else true))))

(def vowel? (complement consonant?))

(defn vowel-in-stem?
  "true iff 0 ... j contains a vowel"
  [stemmer]
  (let [j (get-index stemmer)]
    (loop [i 0]
      (cond (> i j) false
            (consonant? stemmer i) (recur (inc i))
            :else true))))

(defn double-c?
  "returns true if this is a double consonant."
  ([stemmer]
    (double-c? stemmer (get-index stemmer)))
  ([stemmer j]
    (and (>= j 1)
         (= (nth (:word stemmer) j)
            (nth (:word stemmer) (dec j)))
         (consonant? stemmer j))))

(defn cvc?
  "true if (i-2 i-1 i) has the form CVC and
  also if the second C is not w, x, or y.
  This is used when trying to restore an *e*
  at the end of a short word.
  E.g.,
    cav(e), lov(e), hop(e), crim(e)
    but snow, box, tray
  "
  ([stemmer]
    (cvc? stemmer (get-index stemmer)))
  ([stemmer i]
    (and (>= i 2)
         (consonant? stemmer (- i 2))
         (vowel? stemmer (dec i))
         (consonant? stemmer i)
         (not (#{\w \x \y} (nth (:word stemmer) i))))))

(defn m
  "Measures the number of consonant sequences between
  the start of word and position j. If c is a consonant
  sequence and v a vowel sequence, and <...> indicates
  arbitrary presence,
    <c><v>       -> 0
    <c>vc<v>     -> 1
    <c>vcvc<v>   -> 2
    <c>vcvcvc<v> -> 3
    ...
  "
  [stemmer]
  (let [
        j (get-index stemmer)
        count-v (fn [n i]
                  (cond (> i j) [:return n i]
                        (vowel? stemmer i) [:break n i]
                        :else (recur n (inc i))))
        count-c (fn [n i]
                  (cond (> i j) [:return n i]
                        (consonant? stemmer i) [:break n i]
                        :else (recur n (inc i))))
        count-cluster (fn [n i]
                        (let [[stage1 n1 i1] (count-c n i)]
                          (if (= stage1 :return)
                            n1
                            (let [[stage2 n2 i2] (count-v (inc n1) (inc i1))]
                              (if (= stage2 :return)
                                n2
                                (recur n2 (inc i2)))))))
        [stage n i] (count-v 0 0)
        ]
    (if (= stage :return)
      n
      (count-cluster n (inc i)))))

(defn ends?
  "true if the word ends with s."
  [stemmer s]
  (let [word (subword stemmer), sv (vec s), j (- (count word) (count sv))]
    (if (and (pos? j) (= (subvec word j) sv))
      [(assoc stemmer :index (dec j)) true]
      [stemmer false])))


