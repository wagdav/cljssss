(ns thewagner.cljssss.snake)

(defn actions
  "Given the game-state return the set of legal moves (new head positions)"
  [state]
  (let [head (get-in state [:you :head])]
    #{(update head :x inc)    ; right
      (update head :x dec)    ; left
      (update head :y inc)    ; up
      (update head :y dec)})) ; down

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
  [{:keys [board you] :as state}]
  (let [head (:head you)
        width (board :width)
        height (board :height)
        dirs (->> (actions state)
                  (filter (partial inside-board? width height))
                  (filter #(not (body? you %)))
                  (map (partial direction head)))]
    (if (empty? dirs)
      {:move (rand-nth ["up" "down" "left" "right"])
       :shout "I have no better move!"}
      {:move (rand-nth dirs)})))

(comment
  (actions {:game {}
            :board {:width 11 :height 11}
            :turn 0
            :you {:head {:x 0 :y 0}
                  :body [{:x 1 :y 0}]}}))
