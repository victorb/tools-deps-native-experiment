(ns borkdude.tdn.main
  (:require [clojure.java.io :as io]
            [clojure.tools.deps.alpha :as deps])
  (:gen-class))

#_(require '[clojure.tools.deps.alpha.extensions :as ext]) ;; somehow requiring this namespace as a side effect helps...
(require '[clojure.edn :as edn]
         '[clojure.pprint :refer [pprint]]
         '[clojure.tools.deps.alpha.util.maven :as mvn])

;; avoid null pointer
(mvn/make-system)

(defn pp-ret [a]
  (pprint a)
  a)

(defn -main [& args]
  (let [arg (first args)
        edn-str (if (.exists (io/file arg))
                  (slurp arg)
                  arg)]
    (prn (-> (edn/read-string edn-str)
             (pp-ret)
             (update :mvn/repos (fn [repos]
                                  (or repos
                                      {"central" {:url "https://repo1.maven.org/maven2/"}
                                       "clojars" {:url "https://repo.clojars.org/"}})))
             (pp-ret)
             (deps/resolve-deps nil)
             (pp-ret)
             (deps/make-classpath nil nil)
             (pp-ret)))))
