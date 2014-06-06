(ns wondercode.models.project
  (:require [schema.core :as s]
            [wondercode.neo4j.node :as node]))

(def project
  {:resource_name s/Str
   :name          s/Str
   :url           s/Str})

(def empty-template
  (zipmap [:resource_name
           :name
           :url] (repeat nil)))

(def label "project")

(def primary-key :resource_name)

(defn insert-into-db
  ([project]
   (:data (node/insert-node label project)))
  ([project & dependent-projects-pk]
   (let [node (insert-into-db project)]
     (doall (map #(node/link-nodes label primary-key (primary-key node) %) (first dependent-projects-pk)))
     node)))

(defn get-from-db
  [property-name property-value]
  (:data (node/fetch-node label property-name property-value)))

(defn get-dependents
  [property-name property-value]
  (->> (node/get-outgoing-linked-nodes label property-name property-value)
       (reduce #(conj %1 (:data %2)) [])))