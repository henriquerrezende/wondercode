(defproject wondercode "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.1.8"]
                 [compojure "1.1.6"]
                 [de.ubercode.clostache/clostache "1.3.1"]]
  :plugins [[lein-ring "0.8.10"]
            [lein-midje "3.1.1"]]
  :profiles
  {:dev {:dependencies [[midje "1.6.0"]
                        [ring-mock "0.1.5"]]}}
  :main wondercode.handler)