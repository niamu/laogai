(ns laogai.core
  (:gen-class)
  (:require [laogai
             [plex :as plex]
             [lights :as lights]
             [steamlink :as steamlink]]
            [overtone.at-at :as at-at]))

(defonce light-timing-pool (at-at/mk-pool))

(defn light-behaviour
  "Adjust the lights if TV is playing media"
  []
  (condp = (or (plex/on?) (steamlink/on?))
    true (cond
           (plex/watching?) (condp = (plex/state)
                              :paused (lights/set! {:on true :bri 1})
                              :buffering (lights/set! {:on true :bri 1})
                              :playing (lights/set! {:on false}))
           (steamlink/on?) (condp = (steamlink/playing?)
                             true (lights/set! {:on false})
                             false (lights/set! {:on true :bri 1}))
           :else (lights/set! {:on true :bri 254 :transitiontime 300}))
    false (lights/set! {:on false})))

(defn start
  "The main behavioural logic for changing lights state"
  []
  (println "The Earth King invites you to Lake Laogai")
  (at-at/every 200
               #(light-behaviour)
               light-timing-pool
               :desc "Light timing pool"))

(defn stop
  []
  (at-at/stop 1 light-timing-pool)
  (println "Stopped"))

(defn -main
  []
  (start))
