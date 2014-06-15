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
     (doall (map #(node/add-dependency-to-node label primary-key (primary-key node) %) (first dependent-projects-pk)))
     node)))

(defn get-from-db
  [resource-name]
  (:data (node/fetch-node label {primary-key resource-name})))

(defn get-dependents
  [resource-name]
  (->> (node/fetch-nodes-required-by label primary-key resource-name)
       (reduce #(conj %1 (:data %2)) [])))