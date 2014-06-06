(ns wondercode.test.models.project
  (:require [midje.sweet :refer :all]
            [wondercode.models.project :refer :all]
            [wondercode.neo4j.node :as node]))

(with-state-changes
  [(before :facts (do (node/connect-db)
                      (node/delete-all)
                      (node/create-unique-index "project" :resource_name)))
   (after :facts (do (node/delete-all)
                     (node/drop-unique-index "project" :resource_name)))]

  (def wondercode {:resource_name "wondercode"
                   :name          "Wondercode"
                   :url           "https://github.com/henriquerrezende/wondercode"})
  (def neo4j {:resource_name "neo4j"
              :name          "Neo4j"
              :url           "https://github.com/neo4j/neo4j"})
  (def midj {:resource_name "midje"
             :name          "Midje"
             :url           "https://github.com/marick/Midje"})

  (fact "project is persisted and retrieved from db"
        (insert-into-db wondercode)
        (get-from-db :resource_name "wondercode") => (contains {:name "Wondercode"}))

  (fact "insert a project with 2 other dependent projects"
        (let [neo4j-pk (primary-key (insert-into-db neo4j))
              midje-pk (primary-key (insert-into-db midj))
              wondercode-pk (primary-key (insert-into-db wondercode [neo4j-pk midje-pk]))
              dependents (get-dependents primary-key wondercode-pk)]
          (first dependents) => (contains {:resource_name "neo4j"})
          (second dependents) => (contains {:resource_name "midje"}))))