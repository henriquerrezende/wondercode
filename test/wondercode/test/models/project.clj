(ns wondercode.test.models.project
  (:use [midje.sweet]
        [wondercode.models.project])
  (:require [wondercode.mongo-db :as mongo]))

(fact "project is persisted into db"
      (def sample-project (merge empty-template {:name "Project-Name"
                                                 :url  "Project-URL"}))

      (mongo/connect-db)
      (insert-into-db sample-project)
      (last (get-from-db {:url "Project-URL"})) => (contains {:name "Project-Name"}))