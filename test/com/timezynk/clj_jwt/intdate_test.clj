(ns com.timezynk.clj-jwt.intdate-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.timezynk.clj-jwt.intdate :refer [java-time->seconds seconds->java-time]])
  (:import [java.time ZonedDateTime]))

(deftest convert-time
  (testing "java-time->seconds should work fine."
    (let [d (ZonedDateTime/parse "2000-01-02T03:04:05Z")]
      (is (= 946782245 (java-time->seconds d)))
      (is (thrown? AssertionError (java-time->seconds nil)))))
  (testing "seconds->java-time should work fine."
    (let [d (ZonedDateTime/parse "2000-01-02T03:04:05Z")
          i (java-time->seconds d)]
      (is (= d (seconds->java-time i)))
      (is (thrown? AssertionError (seconds->java-time nil))))))
