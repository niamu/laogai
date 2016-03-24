(ns laogai.core
  (:require [laogai
             [tv :as tv]
             [plex :as plex]
             [lights :as lights]]
            [overtone.at-at :as at-at]))

(defonce tv-timing-pool (at-at/mk-pool))
(defonce light-timing-pool (at-at/mk-pool))

(defn tv-behaviour
  "Turn the TV on/off if the lights are on/off"
  []
  (if (lights/reachable?)
    (tv/on!)
    (when-not (plex/watching?)
      (tv/off!))))

(defn light-behaviour
  "Adjust the lights if TV is playing media (on Plex)"
  []
  (when (tv/on?)
    (if (plex/watching?)
      (condp = (plex/state)
        :paused (lights/set! {:on true :bri 1})
        :playing (lights/set! {:on false}))
      (lights/set! {:on true :bri 254 :transitiontime 300}))))

(defn -main
  "The main behavioural logic for changing the TV and lights state"
  []
  (println "The Earth King invites you to Lake Laogai")
  (tv/init)
  (at-at/every 1000
               #(tv-behaviour)
               tv-timing-pool
               :desc "TV timing pool")
  (at-at/every 200
               #(light-behaviour)
               light-timing-pool
               :desc "Light timing pool"))
