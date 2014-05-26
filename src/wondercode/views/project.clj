(ns wondercode.views.project
  (:require [wondercode.template :as template]))

(defn index
  []
  (template/render "index" {:greeting "Hello"}))

(defn new
  [{name "name" url "url"}]
  (println (str "Project " name " " url " added"))
  (template/render "index" {:greeting "Hello"
                            :message  (str "Project " name " " url " added")}))