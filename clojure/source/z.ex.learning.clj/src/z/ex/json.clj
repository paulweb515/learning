(ns z.ex.json)

(require '[clj-http.client :as http])
(require '(clj-json [core :as json]))

(def tst [{"user" "anthonyh", "name" "Anthony", "email" "anthonyh@ca.ibm.com"},
          {"user" "bbokowski", "name" "Boris", "email" "Boris_Bokowski@ca.ibm.com"}])
(defn run-tst
  ""
  []
  (println (get-user-map tst)))

(defn get-component
  ""
  [user-map entry]
  (str "Component: " (get entry "component") " Owner: " (get user-map (get entry "assignee"))))

(defn get-user-map
  ""
  [user-array]
  (reduce (fn [accum x] (assoc accum
                               (get x "user")
                               (get x "name")))
          {}
          user-array))


(defn parse-component
  ""
  [str]
  (let [component-json (json/parse-string (clojure.string/replace str #"\\\u000a" ""))]
    (map (fn [entry] (get-component (get-user-map (get component-json "users")) entry)) (get component-json "assignments") )))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [x (http/get "http://www.eclipse.org/eclipse/platform-ui/componentAreas.json")]
        (parse-component (:body x))))
