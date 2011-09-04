(ns mkvrename.core
  (:use [useful.seq :only [separate]]
        [clojure.java.io :only [file]])
  (:gen-class))

(def cd (System/getProperty "user.dir"))

(defn rename [template n f]
  (let [new-file (file cd (str template n ".mkv"))]
    (println (str "Renaming " (.getName f) " to " (.getName new-file)))
    (.renameTo f new-file)))

(defn prompt-read [& strs]
  (map (fn [s]
         (print s)
         (flush)
         (read-line))
       strs))

(defn template [f]
  (if f
    (re-find #".*- S\d+E" (.getName f))
    (apply format "%s - S%sE" (prompt-read "Show Title: " "This Season: "))))

(defn -main [& args]
  (let [[new done] (separate #(.startsWith (.getName %) "title")
                             (filter (comp (partial re-find #".*\.mkv") str)
                                     (file-seq (file cd))))
        done-count (inc (count done))
        template (template (first done))]
    (doall
     (map (partial rename template)
          (range done-count (+ (count new) done-count))
          new))))