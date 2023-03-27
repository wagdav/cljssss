(ns user
  (:require [io.pedestal.http :as http]
            [thewagner.cljssss.web :as web]))

(def srv (atom nil))

(defn start! []
  (when-not @srv
    (swap! srv web/run-dev)))

(defn stop! []
  (http/stop @srv)
  (reset! srv nil))

(comment
  (start!)
  (stop!))
