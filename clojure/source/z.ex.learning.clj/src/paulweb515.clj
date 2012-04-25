(ns paulweb515)

(defrecord Address [street city])

(defrecord Employer [name address])

(defrecord Person [name dob employer])

(def paul-person 
  (Person. "Paul Webster",
           "1970-08-20",
           (Employer. "IBM",
                      (Address. "770 Palladium Drive",
                                "Ottawa"))))

(def paul-person-prov 
  (let [p paul-person, emp (:employer paul-person), addr (:address emp)]
    (assoc paul-person :employer 
           (assoc emp :address 
                  (assoc addr :province "ON")))))

(def n1 5)
(def n2 3)
(defn n-add-five
  [num]
  (+ num 5))

; (n-add-five n1)


(def a-string "Paul was here")

(def a-length (.length a-string))

(def a-count (count a-string))

(defn to-lower-case
  [token-str]
  (.toLowerCase token-str))

(def pre-tokenized-list 
  '("Paul" "was" "here"))

(def processed-list 
  (map to-lower-case pre-tokenized-list))


(def stop-words
  #{"a" "in" "that" "for" "was" "is"
    "it" "the" "of" "and" "to" "he"})

; (contains? stop-words "and")

(def my-integers
  '(-3 -2 -1 0 1 2 3))

(def my-natural-numbers
  (filter pos? my-integers))


; this doesn't do what I think it does
(def set-complement
  (complement (contains? stop-words "that")))

; this doesn't do what I think it does
(defn set-complement-2
  [stop-words]
  (complement (contains? stop-words "that")))

; this should work
(def not-zero? (complement zero?))

; (not-zero? 0)

(defn say-hi
  ([]
  (say-hi "world"))
  ([name]
  (println (str "Hello, " name "!"))))

; (say-hi)
; (say-hi "Paul")

(def person {:age 42 :name "Paul Webster"})
(let [age (:age person)]
  (if age
    (str "My age is " age)
    "No age given"))


(let [greet (fn [n] (str "Hello, " n))]
  (map greet '(Paul Remy)))


(defn count-item [sequence item]
  "If empty, return 0.  If found, return +1 of the rest  of the list count.  if not found, return the rest of the list count"
  (if (not (seq sequence))
    0
    (if (= (peek sequence) item)
      (inc (count-item (pop sequence) item))
      (count-item (pop sequence) item))))

; recur below says this is tail-recursion
; we're passing in an accumulater argument ... apparently
(defn count-item-recur [sequence item accum]
         (if (not (seq sequence))              
           accum                                 
           (if (= (peek sequence) item)          
             (recur (pop sequence) item (inc accum))
             (recur (pop sequence) item accum))))   

; function to hide the extra argument
(defn count-item-hide 
  [sequence item]
  (count-item-recur sequence item 0))

(defn count-item-loop [sequence item]
  (loop [sq sequence, accum 0]
    (if (not (seq sq))
      accum
      (if (= (peek sq) item)
        (recur (pop sq) (inc accum))
        (recur (pop sq) accum)))))

(defn count-item-cond 
  [sequence item]
  (loop [sq sequence, accum 0]
    (cond (not (seq sq)) accum
          (= (peek sq) item) (recur (pop sq) (inc accum))
          :else (recur (pop sq) accum))))



; recur below says this is tail-recursion
(defn member? [sequence item]          
         (if (not (seq sequence))               
           nil                                    
           (if (= (peek sequence) item)           
             sequence                               
             (recur (pop sequence) item))))


(defn member-cond?
  [sequence item]
  (loop [sq sequence]
    (cond (not (seq sq)) nil
          (= (peek sq) item) sq
          :else (recur (pop sq)))))

(defn count-item-internal-fn 
  [sequence item]
  (let [ci (fn [sq accum]
             (cond (not (seq sq)) accum
                   (= (peek sq) item) (recur (pop sq) (inc accum))
                   :else (recur (pop sq) accum)))]
    (ci sequence 0)))


(defn my-vec [in]
     [in (count in)])

(def my-vec-output
  (let  [ [word length] (my-vec "Paul") ]
     (str "I found " length " chars in " word)))



