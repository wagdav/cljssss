(ns thewagner.cljssss.snake
  (:require [thewagner.cljssss.search :as search]))

(defn self-collision?
  ([state]
   (self-collision? state (state :you)))
  ([_state snake]
   (< 1 (get (frequencies (snake :body)) (snake :head)))))

(defn snake-collision?
  ([state]
   (snake-collision? state (state :you)))
  ([{:keys [board]} snake]
   (let [head (snake :head)
         id (snake :id)
         snakes (filter #(not= id (:id %)) (board :snakes))]
     (contains? (into #{} (apply concat (map :body snakes)))
                head))))

(defn wall-collision?
 ([state]
  (wall-collision? state (state :you)))
 ([state snake]
  (let [width (get-in state [:board :width])
        height (get-in state [:board :height])
        head (snake :head)
        max-x (dec width)
        max-y (dec height)]
    (not (and (<= 0 (head :x) max-x)
              (<= 0 (head :y) max-y))))))

(defn food? [{:keys [board you]}]
  (some #{(you :head)} (:food board)))

(defn no-health? [state]
  (= 0 (get-in state [:you :health])))

(defn actions
  "Given the game-state return the set of legal moves (new head positions)"
  [state]
  (let [head (get-in state [:you :head])]
    (remove (fn [m] (some #{(:head m)} (get-in state [:you :body])))
      #{{:move "right" :head (update head :x inc)}
        {:move "left"  :head (update head :x dec)}
        {:move "up"    :head (update head :y inc)}
        {:move "down"  :head (update head :y dec)}})))

(defn result
  "Transition model: return the result of a move"
  [{:keys [board you] :as state} {:keys [head]}]
  (let [body (you :body)
        food? (some #{head} (:food board))
        new-body (if food?
                   (into [head] body)
                   (into [head] (butlast body)))
        new-health (if food?
                     100
                     (dec (you :health)))]
    (-> state
        (merge {:you {:head head
                      :body new-body
                      :health new-health}})
        (assoc-in [:board :food] (remove #{head} (:food board)))
        (update :turn inc))))

(defn terminal? [state]
  (or (self-collision? state)
      (wall-collision? state)
      (snake-collision? state)
      (no-health? state)))

(defn utility [state]
  (cond
    (terminal? state) -1
    :else (get-in state [:turn])))

(defn cutoff
  "Returns a predicate to cut off the deepending at most at max-depth"
  [max-depth]
  (fn [state depth]
    (or (>= depth max-depth)
        (terminal? state))))

(defn max-value
  ([state]
   (max-value state 0))
  ([state depth]
   (let [cutoff? (get state ::cutoff (cutoff 4))
         as (actions state)]
     (if (or (cutoff? state depth) (empty? as))
       (utility state)
       (apply max (for [a as]
                    (max-value (result state a) (inc depth))))))))

(defn minimax-decision  ; will use α-β pruning later
  "Returns an action"
  [state]
  (apply max-key #(max-value (result state %))
                 (actions state)))

(defn move
  "Given a game board return the next move"
  [state]
  (let [timeout-ms (get-in state [:game :timeout])
        dls (fn [depth]
              (minimax-decision (assoc state ::cutoff (cutoff depth))))]

    (select-keys
      (search/iterative-deepening dls (* 0.8 timeout-ms))
      [:move :shout])))

(comment
  (def example-state {:game {}
                      :board {:width 11
                              :height 11
                              :snakes [{:id 2 :body [{:x 3 :y 4}]}]}
                      :turn 0
                      :you {:id 1
                            :head {:x 2 :y 0}
                            :body [{:x 1 :y 0} {:x 2 :y 0}]
                            :health 100}})

  (def example2 {:game {}
                 :board {:width 11
                         :height 11}
                 :turn 1
                 :you {:id 1
                       :head {:x 4 :y 0}
                       :body [{:x 4 :y 0} {:x 3 :y 0} {:x 3 :y 1}]
                       :health 99}})
  (move example2)
  (actions example2)
  (wall-collision? (result example2 {:move "down" :head {:x 4 :y -1}}))
  (utility (result example2 {:move "up"   :head {:x 4 :y 1}}))
  (utility (result example2 {:move "down" :head {:x 4 :y -1}}))
  (max-value (result example2 {:move "up"   :head {:x 4 :y 1}}) 0)
  (max-value (result example2 {:move "down" :head {:x 4 :y -1}}) 0)

  (utility (result example-state {:head {:x 10 :y 10}}))
  (wall-collision? example-state)
  (self-collision? example-state)
  (snake-collision? example-state)
  (utility example-state)
  (move example-state))
