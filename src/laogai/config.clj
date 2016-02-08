(ns laogai.config
  (:require [clojure.edn :as edn]))

(def config
  (edn/read-string (slurp "resources/config.edn")))
