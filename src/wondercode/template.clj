(ns wondercode.template
  (:require [clostache.parser :as clostache]
            [clojure.java.io :as io]))

(defn render-page
  "Pass in the template name (a string, sans its .mustache
filename extension), the data for the template (a map), and a list of
partials (keywords) corresponding to like-named template filenames."
  [template data partials]
  (clostache/render-resource
    (str "templates/" template ".mustache")
    data
    (reduce (fn [accum pt] ;; "pt" is the name (as a keyword) of the partial.
              (assoc accum pt (slurp (io/resource (str "templates/"
                                                       (name pt)
                                                       ".mustache")))))
            {}
            partials)))