(ns wondercode.mongo-db
  (:import (org.bson.types ObjectId))
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(def db-atom (atom nil))

(defn connect-db
  []
  (let [conn (mg/connect)
        db (mg/get-db conn "wondercode")]
    (reset! db-atom db)))

(defn insert-into
  [doc map]
  (let [result (mc/insert-and-return @db-atom doc (merge map {:_id (ObjectId.)}))]
    (println (str result " inserted!"))))

(defn get-from
  [doc query]
  (mc/find-maps @db-atom doc query))