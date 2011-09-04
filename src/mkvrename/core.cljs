(ns mkvrename.core
  (:require [cljs.nodejs :as nodejs]))

(def fs (nodejs/require "fs"))
(def path (nodejs/require "path"))

(defn separate [pred s]
  (reduce
   (fn [[x y] e]
     (if (pred e) [(conj x e) y] [x (conj y e)]))
   [[] []]
   s))

(def cd ((.cwd nodejs/process)))

(defn rename [template n f]
  (let [new-file (.join path cd (str template n ".mkv"))]
    (println "Renaming" (.basename path f) "to" (.basename path new-file))
    (.renameSync fs f new-file)))

;; Because something fishy is going on with the args passed to -main,
(def args (drop 2 (seq (.argv nodejs/process))))

(defn template [f]
  (if f
    (re-find #".*- S\d+E" (.basename path f))
    (if-let [[name season] args]
      (str name " - " "S" season "E"))))

(defn file-seq [dir]
  (map #(.join path dir %) (.readdirSync fs dir)))

(defn -main [& _]
  (let [[new done] (separate #(re-find #"title\d+" %)
                             (filter #(= (.extname path %) ".mkv") (file-seq cd)))
        start-count (inc (count done))]
    (if (or (> start-count 1) (= (count args) 2))
      (doall
       (map (partial rename (template (first done)))
            (range start-count (+ (count new) start-count))
            new))
      (println "Please tell me the name of the show and the number of this current season."))))

(set! *main-cli-fn* -main)