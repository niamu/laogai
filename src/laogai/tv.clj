(ns laogai.tv
  (:require [clj-http.client :as http]
            [clojure.data.xml :as xml]
            [me.raynes.conch :refer [with-programs]]))

(defn on!
  []
  (prn "Turning TV on...")
  (with-programs [ssh]
    (ssh "pabu" "./tv.sh on")))

(defn off!
  []
  (prn "Turning TV off...")
  (with-programs [ssh]
    (ssh "pabu" "./tv.sh off")))

(defn on?
  []
  (let [result (with-programs [ssh]
                 (ssh "pabu" "./tv.sh state"))]
    (and (.contains result
                    "power status:")
         (.contains result
                    "on"))))

(def plex
  {:address "10.0.0.10"
   :port 32400})

(def base
  (str "http://"
       (:address plex)
       ":"
       (:port plex)
       "/"))

(defn sessions
  []
  (xml/parse-str
   (:body (http/get (str base "status/sessions")))))

(defn pabu
  []
  (first (filter #(and (= :Player
                          (:tag %))
                       (= "Pabu"
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
  (keyword
   (:state (:attrs (pabu)))))
