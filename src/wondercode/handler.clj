(ns wondercode.handler
  (:use [compojure.core]
        [ring.middleware file file-info stacktrace reload params])
  (:require [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [wondercode.views.project :as project]
            [wondercode.neo4j.seed :as seed]))

(defroutes app-routes
           (GET "/project/:resource_name" [resource_name] (project/show resource_name))
           (GET "/projects" [resource_name] (project/index resource_name))
           (route/resources "/")
           (route/not-found "Not Found"))

(defn app []
  (-> app-routes
      (wrap-params)
      ;wraps so I don't need to reload server when file changes
      (wrap-reload '(wondercode.handler view))
      (wrap-file-info)
      (wrap-stacktrace)))

(defn -main []
  (seed/projects)
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "5000"))]
    (jetty/run-jetty (app) {:port port :join? false})))