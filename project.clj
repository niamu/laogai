(defproject laogai "0.3.0"
  :description "Laogai controls home appliances based on conditions"
  :url "http://github.com/niamu/laogai"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.memoize "0.5.8"]
                 [cheshire "5.5.0"]
                 [clj-http "2.0.0"]
                 [overtone/at-at "1.2.0"]
                 [me.raynes/conch "0.8.0"]]
  :main laogai.core)
