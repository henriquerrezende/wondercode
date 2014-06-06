(ns wondercode.mongo.mongo-db
  (:import (org.bson.types ObjectId))
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(def db-atom (atom nil))
(def conn-atom (atom nil))

(defn connect-db
  []
  (let [conn (mg/connect)
        db (mg/get-db conn "wondercode")]
    (reset! conn-atom conn)
    (reset! db-atom db)))

(defn insert-into
  [doc map]
  (if (vector? map)
    (mc/insert-batch @db-atom doc map)
    (mc/insert-and-return @db-atom doc (merge map {:_id (ObjectId.)}))))

(defn get-from
  [doc query]
  (mc/find-maps @db-atom doc query))

(defn drop-db
  []
  (mg/drop-db @conn-atom "wondercode"))