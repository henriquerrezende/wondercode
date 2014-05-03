(ns wondercode.handler
  (:use compojure.core)
  (:require [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [clostache.parser :as clostache]))

(defn read-template [template-name]
  (slurp (clojure.java.io/resource
           (str "templates/" template-name ".mustache"))))

(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))

(defn index []
  (render-template "index" {:greeting "Bonjour"}))

(defroutes app-routes
           (GET "/" [] (index))
           (route/resources "/")
           (route/not-found "Not Found"))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "5000"))]
    (jetty/run-jetty app-routes {:port port})))