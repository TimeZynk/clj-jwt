(ns com.timezynk.clj-jwt.json-key-fn)

(defn write-key
  [x]
  (cond
    (string? x) (str "\"" x "\"")
    :else (name x)))

(defn read-key
  [x]
  (if-let [y (re-seq #"^\"(.*)\"$" x)]
    (-> y first second)
    (keyword x)))
