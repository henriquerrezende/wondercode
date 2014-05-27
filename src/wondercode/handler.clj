(ns wondercode.handler
  (:use [compojure.core]
        [ring.middleware file file-info stacktrace reload params])
  (:require [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [wondercode.views.project :as project]
            [wondercode.mongo-db :as mongo]
            [wondercode.seeds.projects :as seed]))

(defroutes app-routes
           (GET "/projects" [] (project/index))
           ;(POST "/projects" request (project/new (:params request)))
           (GET "/tags/:tag" [tag] (project/search-by-tags tag))
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
  (mongo/connect-db)
  (seed/projects)
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "5000"))]
    (jetty/run-jetty (app) {:port port :join? false})))