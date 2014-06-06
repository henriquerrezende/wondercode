(ns wondercode.neo4j.seed
  (:require [wondercode.neo4j.node :as node]
            [wondercode.models.project :as project]))

(def project-samples
  [{:resource_name "wondercode"
    :name          "Wondercode"
    :url           "https://github.com/henriquerrezende/wondercode"}
   {:resource_name "midje"
    :name          "Midje"
    :url           "https://github.com/marick/Midje"}])

(defn projects
  []
  (node/connect-db)
  (node/delete-all)
  (node/drop-unique-index "project" :resource_name)
  (node/create-unique-index "project" :resource_name)
  (let [midje-pk (project/primary-key (project/insert-into-db (second project-samples)))]
    (project/insert-into-db (first project-samples) [midje-pk])))