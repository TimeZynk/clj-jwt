(ns com.timezynk.clj-jwt.intdate
  (:import [java.time Instant ZonedDateTime ZoneId]))

(defn- java-time? [x] (= ZonedDateTime (type x)))

(defn java-time->seconds ^Long
  [^ZonedDateTime d]
  {:pre (java-time? d)}
  (-> (Instant/from d)
      (.getEpochSecond)))

(defn seconds->java-time ^ZonedDateTime
  ([^Long i]
   (seconds->java-time i (ZoneId/of "Z")))
  ([^Long i ^ZoneId z]
   {:pre [(integer? i) (pos? i)]}
   (-> (Instant/ofEpochSecond i)
       (.atZone z))))
