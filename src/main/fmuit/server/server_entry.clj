(ns fmuit.server.server-entry
  (:require
    [mount.core :as mount]
    fmuit.server.server)
  (:gen-class))

(defn -main [& args]
  (println "args: " args)
  (mount/start-with-args {:config "config/prod.edn"}))
