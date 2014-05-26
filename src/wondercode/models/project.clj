(ns wondercode.models.project)

(def empty-template
  (zipmap [:name
           :url
           :tags] (repeat nil)))