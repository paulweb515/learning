(ns z.ex.scratch)

; list

(def cars '( "V70", "Cobalt"))

(def people ["Paul", "John", "Eric", "Daniel"])

(def projects #{ "eclipse.platform.ui", 
                "eclipse.platform.ua", 
                "eclipse.platform.runtime", 
                "eclipse.platform.text" } )


(def components { "Commands" "pelder",
                 "Model" "emoffatt",
                 "Dialogs" "drolka",
                 "Perspectives" "emoffatt",
                 "WorkingSets" "pwebster",
                 "PropertiesView" "drolka" })

(defrecord Address [street city])

(defrecord Employer [name address])

(defrecord Person [name dob employer])

(def paul-person 
  (Person. "Paul Webster",
           "1970-08-20",
           (Employer. "IBM",
                      (Address. "770 Palladium Drive",
                                "Ottawa"))))

(def pre-tokenized-list 
  '("Paul" "was" "here"))

(defn to-lower-case
  [token-str]
  (.toLowerCase token-str))

(def processed-list 
  (map to-lower-case pre-tokenized-list))

(def stop-words
  #{"a" "in" "that" "for" "was" "is"
    "it" "the" "of" "and" "to" "he"})

(def person {:age 42 :name "Paul Webster"})

(defn person-age 
  ""
  [person-map]
  (let [age (:age person-map)]
    (if age
      (str "My age is " age)
      "No age given")))

; can't use peek and pop, it's a lazy sequence
; use first and rest instead
(defn count-item [sequence item]
  "If empty, return 0.  If found, return +1 of the rest  of the list count. if not found, return the rest of the list count"
  (if (not (seq sequence))
    0
    (if (= (first sequence) item)
      (inc (count-item (rest sequence) item))
      (count-item (rest sequence) item))))

; recur below says this is tail-recursion
; we're passing in an accumulater argument ... apparently
(defn count-item-recur [sequence item accum]
  (if (not (seq sequence))              
    accum                                 
    (if (= (first sequence) item)          
      (recur (rest  sequence) item (inc accum))
      (recur (rest sequence) item accum))))

(defn count-item-loop [sequence item]
  (loop [sq sequence, accum 0]
    (if (not (seq sq))
      accum
      (if (= (first sq) item)
        (recur (rest sq) (inc accum))
        (recur (rest sq) accum)))))

(defn count-item-cond 
  [sequence item]
  (loop [sq sequence, accum 0]
    (cond (not (seq sq)) accum
      (= (first sq) item) (recur (rest sq) (inc accum))
      :else (recur (rest sq) accum))))

(defn count-item-internal-fn 
  [sequence item]
  (let [ci (fn [sq accum]
             (cond (not (seq sq)) accum
               (= (first sq) item) (recur (rest sq) (inc accum))
               :else (recur (rest sq) accum)))]
    (ci sequence 0)))


(defn format-repo
  ""
  [repo-collection]
  (map (fn [repo] 
         (str "Repo: " repo))
       repo-collection))

(defn print-repo
  ""
  [repo-collection]
  (let [c (format-repo repo-collection)]
    (for [repo c]
      (println  repo))))

(defn print-street
  ""
  [person]
  (let [s1 (get-in person [:employer :address :street])
        s2 (-> person :employer :address :street)
        s3 (reduce get person [:employer :address :street])]
    (println (str "s1: " s1 "\ns2: " s2 "\ns3: " s3))))

(defn test-loop
  ""
  [number]
  (loop [n number factorial 1]
    (if (zero? n)
      factorial
      ; each call to recur re-initializes n and factorial with new values
      (recur (dec n) (* factorial n)))))
