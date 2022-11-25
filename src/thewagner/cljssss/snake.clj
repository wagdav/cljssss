(ns thewagner.cljssss.snake)

(defn actions [{:keys [you]}]
  (let [{:keys [x y]} (:head you)]
    (println "Head is" [x y])))

(defn new-head [{:keys [x y]}]
  (vector {:x (inc x) :y y}
          {:x (dec x) :y y}
          {:x x :y (inc y)}
          {:x x :y (dec y)}))

(defn inside-board? [width height {:keys [x y]}]
  (let [max-x (dec width)
        max-y (dec height)]
    (and (<= 0 x max-x)
         (<= 0 y max-y))))

(defn direction [p1 p2]
  (if (= (:y p1) (:y p2))
    (if (< (:x p1) (:x p2))
      "right"
      "left")
    (if (< (:y p1) (:y p2))
      "up"
      "down")))

(defn body? [snake p]
  (contains?
    (into #{} (:body snake))
    p))

(defn move
  "Given a game board return the next move"
  [{:keys [board you]}]
  (let [head (:head you)
        width (board :width)
        height (board :height)
        dirs (->> (new-head head)
                  (filter (partial inside-board? width height))
                  (filter #(not (body? you %)))
                  (map (partial direction head)))]
    (if (empty? dirs)
      {:move (rand-nth ["up" "down" "left" "right"])
       :shout "I have no better move!"}
      {:move (rand-nth dirs)})))

(comment
  (move {:game {}
         :board {:width 11 :height 11}
         :turn 0
         :you {:head {:x 0 :y 0}
               :body [{:x 1 :y 0}]}}))
