(ns wondercode.template
  (:require [clostache.parser :as clostache]))

(defn- read-template [template-name]
  (slurp (clojure.java.io/resource
           (str "templates/" template-name ".mustache"))))

(defn render [template-file params]
  (clostache/render (read-template template-file) params))
