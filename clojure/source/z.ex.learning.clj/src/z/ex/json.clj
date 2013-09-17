(ns z.ex.json)

(require '[clj-http.client :as http])
(require '(clj-json [core :as json]))

;(defn old-parse-component
;  ""
;  [str]
;  (let [component-json (json/parse-string (clojure.string/replace str #"\\\u000a" ""))]
;    (map (fn [entry] (get-component (get-user-map (get component-json "users")) entry)) (get component-json "assignments") )))

(defn get-component
  ""
  [user-map entry]
  (str "Component: " (get entry "component") 
       " Owner: " (get user-map (get entry "assignee"))))

(defn get-user-map
  ""
  [user-array]
  (reduce (fn [accum x] (assoc accum (get x "user") (get x "name")))
          {}
          user-array))

(defn build-list
  ""
  [json-body]
  (let [component-json (json/parse-string 
                         (clojure.string/replace json-body #"\\\u000a" ""))
        user-map (get-user-map (get component-json "users"))
        assign-array (get component-json "assignments")]
    (map (fn [entry] (get-component user-map entry)) assign-array)))

(defn get-component-json
  "Load the componet json and print out the component vs people"
  [& args]
  (let [x (http/get "http://www.eclipse.org/eclipse/platform-ui/componentAreas.json")]
        (build-list (:body x))))
