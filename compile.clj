(require
  '[clojure.string :as string]
  '[clojure.java.io :as io]
  '[clojure.java.shell :as shell :refer [sh]])

(def tests
  {"incanter" 'incanter.core
   "aleph"    'aleph.http})

(defn classpath
  [profiles]
  (let [profiles (string/join "," (into ["-user" "-base"] (map #(str "+" %) profiles)))
        {:keys [out]} (apply sh ["lein" "with-profile" profiles "classpath"])]
    (string/trim out)))

(defn debug [x] (println x) x)

(defn run-test
  [ns profiles]
  (let [out-dir (io/file "output")
        _ (sh "rm" "-rf" (str out-dir))
        _ (.mkdirs out-dir)
        cp (classpath profiles)
        {:keys [out err]} (apply sh ["java"
                                     "-cp" cp
                                     (format "-Dclojure.compile.path=%s" out-dir)
                                     "clojure.main"
                                     "-e"
                                     (format "(let [start (System/currentTimeMillis)
                                                    _  (compile '%s)
                                                    end (System/currentTimeMillis)]
                                                (println (- end start)))"
                                       ns)])]
    (when-not (or (string/blank? err) (re-seq #"WARNING" err))
      (throw (ex-info (format "Error running test using lein: %s" err)
               {:classpath cp
                :out out
                :err err})))
    [ns (Integer/parseInt (string/trim out))]))

(defn print-csv
  [title seq]
  (println (string/join "," (cons title seq))))

(doseq [[profile ns] tests]
  (dotimes [_ 4]  
    (print-csv "RC1" (run-test ns [profile]))
    (print-csv "RC1+CLJ-703"(run-test ns ["clj-703" profile]))))

