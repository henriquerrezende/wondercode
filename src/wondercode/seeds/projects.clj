(ns wondercode.seeds.projects
  (require [wondercode.models.project :as project]
           [wondercode.mongo-db :as mongo]))

(def project-samples
  [{:name "wondercode"
    :url  "https://github.com/henriquerrezende/wondercode"
    :tags ["clojure", "compojure", "monger"]}
   {:name "clojure-koans"
    :url  "https://github.com/functional-koans/clojure-koans"
    :tags ["clojure"]}])

(defn projects
  []
  (mongo/drop-db)
  (project/insert-into-db project-samples))
