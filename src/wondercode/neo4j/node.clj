(ns wondercode.neo4j.node
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [clojurewerkz.neocons.rest.labels :as nl]
            [clojurewerkz.neocons.rest.index :as ni]
            [clojurewerkz.neocons.rest.constraints :as nc]
            [clojurewerkz.neocons.rest.relationships :as nrel]))

; Abstracting the setup for the label and property-name since we'll always use
; the same (e.g. all projects will be under the label 'project' and connected through
; the property :resource_name)

(def conn-atom (atom nil))
(def index (atom nil))

(defn connect-db
  []
  (let [conn (nr/connect "http://localhost:7474/db/data/")
        ;conn2 (assoc-in conn [:options :debug] true)
        ]
    (reset! conn-atom conn)))

(defn create-unique-index
  [label property-name]
  "Unique index as Neo4j 2.0, so queries by label/property-name are faster. property-name is unique as well."
  (println (str "Creating unique index for label " label " and property " property-name))
  (reset! index (nc/create-unique @conn-atom label property-name)))

(defn drop-all-unique-index
  [label]
  (if-let [label-constraints (ni/get-all @conn-atom label)]
    (dorun (map
             #(let [label (:label %)
                    property-keys (:property_keys %)]
               (println (str "Dropping unique index for label " label " and property-keys " property-keys))
               (nc/drop-unique @conn-atom label (first property-keys)))
             label-constraints))))

(defn delete-all
  []
  (println "Deleting all nodes and relationships")
  (cy/tquery @conn-atom "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r"))

(defn insert-node
  [label node-properties]
  (println (str "Inserting node " label " " node-properties))
  (try
    (let [node (nn/create @conn-atom node-properties)]
      (nl/add @conn-atom node label)
      node)
    (catch Exception e
      (do (println (str "Failed to insert node"))
          (throw e)))))

(defn fetch-node
  [node-label node-properties]
  (let [key (first (keys node-properties))
        value (first (vals node-properties))]
    (first (nl/get-all-nodes @conn-atom node-label key value))))

(defn add-dependency-to-node
  [label property-name from to]
  (println (str "Node " from " require " to))
  (let [from-node (fetch-node label {property-name from})
        to-node (fetch-node label {property-name to})]
    (nrel/create @conn-atom from-node to-node :requires {:created-at (java.util.Date.)})))

(defn fetch-nodes-required-by
  [label property-name from]
  (println (str "Fetching nodes required by " from))
  (let [from-node (fetch-node label {property-name from})]
    (when-let [relationships (nrel/outgoing-for @conn-atom from-node :requires :created-at)]
      (map #(nn/fetch-from @conn-atom (:end %)) relationships))))