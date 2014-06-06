(ns wondercode.test.node
  (:require [midje.sweet :refer :all]
            [wondercode.neo4j.node :refer :all]))

(with-state-changes
  [(before :facts (do (connect-db)
                      (delete-all)
                      (create-unique-index "node_label" :node_property)))
   (after :facts (do (delete-all)
                     (drop-unique-index "node_label" :node_property)))]

  (fact "connect into neo4j, create a node and fetch it"
        (insert-node "node_label" {:node_property "neo4j"
                                   :url           "https://github.com/neo4j/neo4j"})
        (:data (fetch-node "node_label" :node_property "neo4j"))
        => {:node_property "neo4j" :url "https://github.com/neo4j/neo4j"})

  (fact "link two nodes"
        (insert-node "node_label" {:node_property "neo4j"
                                   :url           "https://github.com/neo4j/neo4j"})
        (insert-node "node_label" {:node_property "midje"
                                   :url           "https://github.com/marick/Midje"})
        (insert-node "node_label" {:node_property "wondercode"
                                   :url           "https://github.com/henriquerrezende/wondercode"})
        (link-nodes "node_label" :node_property "wondercode" "neo4j")
        (link-nodes "node_label" :node_property "wondercode" "midje")
        (let [nodes (get-outgoing-linked-nodes "node_label" :node_property "wondercode")]
          (:data (first nodes)) => {:node_property "neo4j"
                                    :url           "https://github.com/neo4j/neo4j"}
          (:data (second nodes)) => {:node_property "midje"
                                     :url           "https://github.com/marick/Midje"})))