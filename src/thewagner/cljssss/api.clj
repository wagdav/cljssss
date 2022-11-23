(ns thewagner.cljssss.api
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [io.pedestal.log :as log]))

(defn info
  "https://docs.battlesnake.com/api/requests/info"
  [request]
  {:status 200
   :body {:apiversion   "1"
          :author       "wagdav"
          :color        "#888888"
          :head         "default"
          :tail         "default"
          :version      "0.0.1-beta"}})

(defn start
  "https://docs.battlesnake.com/api/requests/start"
  [request]
  (log/info :msg "Start")
  {:status 200})

(defn move
  "https://docs.battlesnake.com/api/requests/move"
  [request]
  {:status 200
   :body {:move "up"
          :shout "Moving up!"}})

(defn end
  "https://docs.battlesnake.com/api/requests/end"
  [request]
  (log/info :msg "End")
  {:status 200})

(def as-json [(body-params/body-params) http/json-body])

(def routes
  #{["/"      :get  (conj as-json `info)]
    ["/start" :post (conj as-json `start)]
    ["/move"  :post (conj as-json `move)]
    ["/end"   :post (conj as-json `end)]})
