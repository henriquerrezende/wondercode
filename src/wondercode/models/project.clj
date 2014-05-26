(ns wondercode.models.project
  (:require [schema.core :as s]
            [wondercode.mongo-db :as mongo]))

(def project
  {:name                  s/Str
   :url                   s/Str
   (s/optional-key :tags) [s/Str]})

(def empty-template
  (zipmap [:name
           :url
           :tags] (repeat nil)))

(defn insert-into-db
  [project]
  (mongo/insert-into "project" project))

(defn get-from-db
  ([query]
   (mongo/get-from "project" query))
  ([]
   (get-from-db {})))
