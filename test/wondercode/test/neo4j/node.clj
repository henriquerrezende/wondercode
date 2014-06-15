(ns wondercode.test.neo4j.node
  (:require [midje.sweet :refer :all]
            [wondercode.neo4j.node :refer :all]))

(with-state-changes
  [(before :facts (do (connect-db)
                      (delete-all)
                      (drop-all-unique-index "node_label")
                      (create-unique-index "node_label" :node_property)))
   (after :facts (do (delete-all)
                     (drop-all-unique-index "node_label")))]

  (fact "connect into neo4j, create a node and fetch it"
        (insert-node "node_label" {:node_property "neo4j"})
        (:data (fetch-node "node_label" {:node_property "neo4j"}))
        => {:node_property "neo4j"})

  (fact "a graph can't have two nodes with same node_property"
        (insert-node "node_label" {:node_property "neo4j"})
        (insert-node "node_label" {:node_property "neo4j"}) =>
        (throws Exception)
        (:data (fetch-node "node_label" {:node_property "neo4j"}))
        => {:node_property "neo4j"})

  (fact "a node can have a dependency node"
        (insert-node "node_label" {:node_property "neo4j"})
        (insert-node "node_label" {:node_property "midje"})
        (insert-node "node_label" {:node_property "wondercode"})
        (add-dependency-to-node "node_label" :node_property "wondercode" "neo4j")
        (add-dependency-to-node "node_label" :node_property "wondercode" "midje")
        (let [nodes (fetch-nodes-required-by "node_label" :node_property "wondercode")]
          (:data (first nodes)) => {:node_property "neo4j"}
          (:data (second nodes)) => {:node_property "midje"})))