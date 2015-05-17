(defproject clj-703 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]]
  :source-paths []
  :resource-paths []
  :test-paths []
  
  :profiles {:clj-703       {:dependencies ^:replace [[ragge/clojure "1.7.0-clj703-SNAPSHOT"]]}
             :aleph         {:dependencies [[aleph "0.4.0"]]}
             :incanter      {:dependencies [[incanter "1.5.6"]]}})
