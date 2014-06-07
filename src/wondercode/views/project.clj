(ns wondercode.views.project
  (:require [wondercode.template :as template]
            [wondercode.models.project :as project]))

(defn show
  [resource_name]
  (template/render-page
    "project/show"
    {:projects (project/get-from-db :resource_name resource_name)}
    [:header :footer]))

(defn index
  [resource_name]
  (template/render-page
    "project/index"
    {}
    [:header :footer]))