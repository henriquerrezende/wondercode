(ns wondercode.neo4j.node
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [clojurewerkz.neocons.rest.labels :as nl]
            [clojurewerkz.neocons.rest.constraints :as nc]
            [clojurewerkz.neocons.rest.relationships :as nrel]
            [clojurewerkz.neocons.rest.records :as nrec]))

(def conn-atom (atom nil))

(defn connect-db
  []
  (let [conn (nr/connect "http://localhost:7474/db/data/")
        ;conn2 (assoc-in conn [:options :debug] true)
        ]
    (reset! conn-atom conn)))

(defn create-unique-index
  [label property-name]
  (println (str "Creating unique index for label " label " and property " property-name))
  (nc/create-unique @conn-atom label property-name))

(defn drop-unique-index
  [label property-name]
  (println (str "Dropping unique index for label " label " and property " property-name))
  (nc/drop-unique @conn-atom label property-name))

(defn delete-all
  []
  (println "Deleting all nodes and relationships")
  (cy/tquery @conn-atom "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r"))

(defn insert-node
  [label node-properties]
  (println (str "Inserting node " label " " node-properties))
  (let [id (nn/create @conn-atom node-properties)]
    (nl/add @conn-atom id label)
    id))

(defn fetch-node
  [label property-name property-value]
  ;(println (str "Fetching node " label " with " (name property-name) " : " (name property-value)))
  (-> (cy/tquery
        @conn-atom
        (str "MATCH (n:" label ") WHERE n." (name property-name) " = { " (name property-name) " } RETURN n")
        {property-name property-value})
      (first)
      (get "n")
      (nrec/instantiate-node-from)))

(defn link-nodes
  [label property-name from to]
  (println (str "Linking nodes from " from " to " to))
  (let [from-node (fetch-node label property-name from)
        to-node (fetch-node label property-name to)]
    (nrel/create @conn-atom from-node to-node :links {:created-by "Henrique"})))

(defn get-outgoing-linked-nodes
  [label property-name from]
  (println (str "Getting outgoing linked nodes from " property-name " " from " and label " label))
  (let [from-node (fetch-node label property-name from)]
    (when-let [relationships (nrel/outgoing-for @conn-atom from-node :links :created-by)]
      (map #(nn/fetch-from @conn-atom (:end %)) relationships))))