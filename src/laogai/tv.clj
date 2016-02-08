(ns laogai.tv
  (:require [laogai.config :refer [config]]
            [clj-http.client :as http]
            [clojure.data.xml :as xml]
            [me.raynes.conch :refer [with-programs]]))

(defonce power-state
  (atom {:on false}))

(defn on!
  []
  (prn "Turning TV on...")
  (with-programs [ssh]
    (ssh (-> config :rpi :addr) "./tv.sh on"))
  (swap! power-state assoc :on true))

(defn off!
  []
  (prn "Turning TV off...")
  (with-programs [ssh]
    (ssh (-> config :rpi :addr) "./tv.sh off"))
  (swap! power-state assoc :on false))

(defn on?
  []
  (:on @power-state))

(def base
  (str "http://"
       (-> config :plex :addr)
       ":"
       (-> config :plex :port)
       "/"))

(defn sessions
  []
  (xml/parse-str
   (:body (http/get (str base "status/sessions")))))

(defn pabu
  []
  (first (filter #(and (= :Player
                          (:tag %))
                       (= (-> config :plex :client)
                          (:title (:attrs %))))
                 (-> (sessions)
                     :content
                     first
                     :content))))

(defn watching?
  []
  (not (empty? (pabu))))

(defn state
  []
  (keyword (:state (:attrs (pabu)))))
