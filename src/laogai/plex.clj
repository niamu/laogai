(ns laogai.plex
  (:require [laogai.config :refer [config]]
            [clj-http.client :as http]
            [clojure.data.xml :as xml])
  (:import [java.net InetAddress]))

(def plex
  (-> config :plex))

(def base
  (str "http://" (:addr plex) ":" (:port plex) "/"))

(defn on?
  "Returns true if Plex client is currently connected
  Note: This has a significant delay to disappear from the list"
  []
  (some #(= (:client plex)
            (-> % :attrs :name))
        (-> (http/get (str base "clients")
                      {:retry-handler
                       (fn [ex try-count http-context]
                         (println "Plex Error:" ex)
                         (if (> try-count 4) false true))})
            :body xml/parse-str :content)))

(defn sessions
  "Parse returned XML of all current Plex sessions"
  []
  (-> (http/get (str base "status/sessions")
                {:retry-handler
                 (fn [ex try-count http-context]
                   (println "Plex Error:" ex)
                   (if (> try-count 4) false true))})
      :body xml/parse-str))

(defn client
  "Returns the parsed XML contents of the Plex client session"
  []
  (first (filter #(and (= :Player (:tag %))
                       (= (:client plex)
                          (:title (:attrs %))))
                 (-> (sessions) :content first :content))))

(defn watching?
  "Returns boolean value dependent on whether the Plex client is in a session"
  []
  (not (empty? (client))))

(defn state
  "Returns the current state (playing/paused) of the Plex client as a keyword"
  []
  (keyword (:state (:attrs (client)))))
