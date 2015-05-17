declare -A tests
tests[aleph]=aleph.http
tests[incanter]=incanter.core


for i in "${!tests[@]}"
do
    for n in {1..4}; do
        test=$i
        namespace=${tests[$i]}
        
        mkdir -p output-rc1
        rm -rf output-rc1/*
        
        java -cp $(lein with-profile ${test} classpath) \
             -Dclojure.compile.path=output-rc1 \
             clojure.main -e "(let [start (System/currentTimeMillis)
                                    ns '${namespace}
                                    _  (compile ns)
                                    end (System/currentTimeMillis)]
                                (println (clojure.string/join \",\" [\"RC1\" (name ns) (- end start)])))"
        
        mkdir -p output-clj-703
        rm -rf output-clj-703/*
        
        java -cp $(lein with-profile clj-703,${test} classpath) \
             -Dclojure.compile.path=output-clj-703 \
             clojure.main -e "(let [start (System/currentTimeMillis)
                                    ns '${namespace}
                                    _  (compile ns)
                                    end (System/currentTimeMillis)]
                                (println (clojure.string/join \",\" [\"RC1+CLJ-703\" (name ns) (- end start)])))"
    done
done
