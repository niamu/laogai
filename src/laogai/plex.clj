(ns laogai.plex
  (:require [laogai.config :refer [config]]
            [clj-http.client :as http]
            [clojure.data.xml :as xml]))

(def plex
  (-> config :plex))

(def base
  (str "http://" (:addr plex) ":" (:port plex) "/"))

(defn sessions
  "Parse returned XML of all current Plex sessions"
  []
  (xml/parse-str
   (:body (http/get (str base "status/sessions")))))

(defn client
  "Returns the parsed XML contents of the Plex client session"
  []
  (first (filter #(and (= :Player (:tag %))
                       (= (:client plex)
                          (:title (:attrs %))))
                 (-> (sessions)
                     :content
                     first
                     :content))))

(defn watching?
  "Returns boolean value dependent on whether the Plex client is in a session"
  []
  (not (empty? (client))))

(defn state
  "Returns the current state (playing/paused) of the Plex client as a keyword"
  []
  (keyword (:state (:attrs (client)))))