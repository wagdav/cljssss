(ns thewagner.cljssss.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]))

(def max-coords 100)

(s/def ::number (s/int-in 0 max-coords))
(s/def ::x ::number)
(s/def ::y ::number)
(s/def :battlesnake/coordinate (s/keys :req-un [::x ::y]))

(s/def ::id string?)
(s/def ::name string?)
(s/def ::health (s/int-in 0 100))
(s/def ::body (s/coll-of :battlesnake/coordinate))
(s/def ::latency (s/with-gen string? #(gen/fmap str gen/nat)))
(s/def ::head :battlesnake/coordinate)
(s/def ::length nat-int?)
(s/def ::shout string?)
(s/def ::squad string?)
(s/def :battlesnake/snake
  (s/with-gen
    (s/keys :req-un [::id ::name ::health ::body ::latency ::head ::length
                     ::shout ::squad])
    #(gen/let [{:keys [body] :as snake} (gen/hash-map :id (s/gen ::id)
                                                      :name (s/gen ::name)
                                                      :health (s/gen ::health)
                                                      :body (s/gen ::body)
                                                      :latency (s/gen ::latency)
                                                      :shout (s/gen ::shout)
                                                      :squad (s/gen ::squad))]
       (merge snake
              {:head (first body)
               :length (count body)}))))

(s/def ::width ::number)
(s/def ::height ::number)
(s/def ::snakes (s/coll-of :battlesnake/snake :min-count 1 :max-count 16))
(s/def :battlesnake/board (s/keys :req-un [::width ::height ::snakes]))

(comment
  (gen/generate (s/gen ::latency))
  (gen/generate (s/gen :battlesnake/snake))
  (gen/generate (s/gen :battlesnake/board)))
