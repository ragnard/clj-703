#!/bin/bash

java -cp $(lein classpath) clojure.main -i compile.clj

