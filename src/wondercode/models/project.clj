(ns wondercode.models.project
  (:require [schema.core :as s]))

(def project
  {:name s/Str
   :url s/Str
   (s/optional-key :tags) [s/Str]})

(def empty-template
  (zipmap [:name
           :url
           :tags] (repeat nil)))