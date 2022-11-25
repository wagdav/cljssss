(ns thewagner.cljssss.snake)

(defn self-collision? [state]
  (let [head (get-in state [:you :head])
        body (get-in state [:you :body])]
    (contains? (into #{} body) head)))

(defn wall-collision? [state]
  (let [width (get-in state [:board :width])
        height (get-in state [:board :height])
        head (get-in state [:you :head])
        max-x (dec width)
        max-y (dec height)]
    (not (and (<= 0 (head :x) max-x)
              (<= 0 (head :y) max-y)))))

(defn actions
  "Given the game-state return the set of legal moves (new head positions)"
  [state]
  (let [head (get-in state [:you :head])]
    #{(update head :x inc)    ; right
      (update head :x dec)    ; left
      (update head :y inc)    ; up
      (update head :y dec)})) ; down

(defn result
  "Transition model: return the result of a move"
  [state action]
  (assoc-in state [:you :head] action))

(defn utility [state]
  (cond
    (self-collision? state) -1
    (wall-collision? state) -1
    :else (rand 5)))

(defn direction [p1 p2]
  (if (= (:y p1) (:y p2))
    (if (< (:x p1) (:x p2))
      "right"
      "left")
    (if (< (:y p1) (:y p2))
      "up"
      "down")))

(defn move
  "Given a game board return the next move"
  [{:keys [board you] :as state}]
  (let [head (:head you)
        width (board :width)
        height (board :height)
        s (->> (actions state)
               (map #(result state %))
               (apply max-key utility))
        new-head (get-in s [:you :head])
        dir (direction head new-head)]
   {:move dir}))

(comment
  (def example-state {:game {}
                      :board {:width 11 :height 11}
                      :turn 0
                      :you {:head {:x 0 :y 0}
                            :body [{:x 1 :y 0}]}})
  (actions example-state)
  (result example-state {:x 10 :y 10})
  (wall-collision? example-state)
  (utility example-state)
  (move example-state))
