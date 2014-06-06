(ns wondercode.views.project
  (:require [wondercode.template :as template]
            [wondercode.models.project :as project]
            [monger.operators :refer :all]))

(defn index
  [resource_name]
  (template/render "index" {:projects (project/get-from-db :resource_name resource_name)}))

;(defn new
;  [{name "name" url "url"}]
;  (println (str "Project " name " " url " added"))
;  (template/render "index" {:greeting "Hello"
;                            :message  (str "Project " name " " url " added")}))

(defn search-by-tags
  [tags]
  ;TODO: Remove dependency on monger
  (let [query {:tags {$in [tags]}}]
    (template/render "index" {:projects (project/get-from-db query)})))
