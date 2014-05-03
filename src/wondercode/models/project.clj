(ns wondercode.models.project.project)

(def empty-template
  (zipmap [:name
           :url
           :tags] (repeat nil)))