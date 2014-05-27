(ns wondercode.test.models.project
  (:use [midje.sweet]
        [wondercode.models.project])
  (:require [wondercode.mongo-db :as mongo]
            [monger.operators :refer :all]))

(with-state-changes
  [(before :facts (do (mongo/connect-db)
                      (mongo/drop-db)))
   (after :contents (mongo/drop-db))]

  (def sample-projects [{:name "Project Name"
                         :url  "project-url"
                         :tags ["tag1" "tag2"]}
                        {:name "Another Project Name"
                         :url  "another-project-url"
                         :tags ["tag2"]}])

  (fact "project is persisted and retrived from db"
        (insert-into-db (first sample-projects))
        (last (get-from-db {:url "project-url"})) => (contains {:name "Project Name"}))

  (fact "more than one project is persisted into db in a single transaction"
        (insert-into-db sample-projects)
        (last (get-from-db {:url "project-url"})) => (contains {:name "Project Name"})
        (last (get-from-db {:url "another-project-url"})) => (contains {:name "Another Project Name"}))

  (fact "retrieve all projects"
        (insert-into-db sample-projects)
        (count (get-from-db)) => 2)

  (fact "retrieve projects by tag"
        (insert-into-db sample-projects)
        (count (get-from-db {:tags {$in ["tag1"]}})) => 1
        (count (get-from-db {:tags {$in ["tag2"]}})) => 2))