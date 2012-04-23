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


(defn say-hi
  ([]
  (say-hi "world"))
  ([name]
  (println (str "Hello, " name "!"))))

; (say-hi)
; (say-hi "Paul")



