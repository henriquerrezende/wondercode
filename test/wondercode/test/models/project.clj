(ns wondercode.test.models.project
  (:require [midje.sweet :refer :all]
            [wondercode.models.project :refer :all]
            [wondercode.neo4j.node :as node]))

(with-state-changes
  [(before :facts (do (node/connect-db)
                      (node/delete-all)
                      (node/drop-all-unique-index "project")
                      (node/create-unique-index "project" :resource_name)))
   (after :facts (do (node/delete-all)
                     (node/drop-all-unique-index "project")))]

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
        (get-from-db "wondercode") => (contains {:name "Wondercode"}))

  (fact "insert a project with 2 other dependent projects"
        (let [neo4j-pk (primary-key (insert-into-db neo4j))
              midje-pk (primary-key (insert-into-db midj))]
          (insert-into-db wondercode [neo4j-pk midje-pk])
          (get-dependents "wondercode") => (has some (and (contains {:resource_name "neo4j"})
                                                          (contains {:resource_name "midje"}))))))