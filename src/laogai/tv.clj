(ns laogai.tv
  (:require [laogai.config :refer [config]]
            [clojure.string :as string]
            [me.raynes.conch :refer [with-programs]])
  (:import clojure.lang.ExceptionInfo))

(def rpi
  (-> config :rpi))

(defonce power-state
  (atom {:on nil}))

(defonce input
  (atom nil))

(defn state
  []
  (with-programs [ssh]
    (-> (try (ssh {:timeout 1000} (:addr rpi) "./tv.sh state")
             (catch ExceptionInfo e
               ;; Assume the TV is on
               (str "state: on")))
        (string/split #":")
        last
        (string/trim)
        keyword)))

(defn init
  []
  (swap! power-state assoc :on (= :on (state))))

(defn switch-input!
  [input-select]
  (condp = input-select
    :steam (comment "No need to handle this case for now. It will never happen")
    :plex (when-not (= input-select @input)
            (with-programs [ssh]
              (try (ssh (:addr rpi) "./tv.sh active-input")
                   (catch ExceptionInfo e
                     :error)))))
  (reset! input input-select))

(defn on?
  "Returns boolean value of current TV power state"
  []
  (:on @power-state))

(defn on!
  "Turn the TV on via SSH command to the Raspberry Pi"
  []
  (when (false? (on?))
    (prn "Turning TV on...")
    (swap! power-state assoc :on true)
    (with-programs [ssh]
      (try (ssh (:addr rpi) "./tv.sh on")
           (catch ExceptionInfo e
             :error)))))

(defn off!
  "Turn the TV off via SSH command to the Raspberry Pi"
  []
  (when (true? (on?))
    (prn "Turning TV off...")
    (swap! power-state assoc :on false)
    (with-programs [ssh]
      (try (ssh (:addr rpi) "./tv.sh off")
           (catch ExceptionInfo e
             :error)))))
