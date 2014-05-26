(ns wondercode.views.project
  (:require [wondercode.template :as template]
            [wondercode.models.project :as project]))

(defn index
  []
  (template/render "index" {:projects (project/get-from-db)}))

(defn new
  [{name "name" url "url"}]
  (println (str "Project " name " " url " added"))
  (template/render "index" {:greeting "Hello"
                            :message  (str "Project " name " " url " added")}))