(defproject com.timezynk/clj-jwt "0.1.2"
  :description  "Clojure library for JSON Web Token(JWT). Forked from https://github.com/liquidz/clj-jwt which is no longer active."
  :url          "https://github.com/liquidz/clj-jwt"
  :license      {:name "MIT"
                 :url  "https://mit-license.org"}
  :scm          {:name "git"
                 :url  "https://github.com/TimeZynk/clj-jwt"}
  :dependencies [[ch.qos.logback/logback-core "1.2.9"]
                 [ch.qos.logback/logback-classic "1.2.9"]
                 [ch.qos.logback.contrib/logback-jackson "0.1.5"]
                 [ch.qos.logback.contrib/logback-json-classic "0.1.5"]
                 [clojure.java-time "1.2.0"]
                 [crypto-equality "1.0.1"]
                 [org.apache.logging.log4j/log4j-to-slf4j "2.17.0"]
                 [org.clojure/clojure "1.11.1"]
                 [org.clojure/data.codec "0.1.1"]
                 [org.clojure/data.json "2.4.0"]
                 [org.clojure/tools.logging "1.2.4"]
                 [org.bouncycastle/bcpkix-jdk15on "1.52"]]
  :plugins      [[lein-cljfmt "0.9.0"]]
  :repl-options {:init-ns com.timezynk.clj-jwt}
  :profiles     {:dev {:dependencies []}})
