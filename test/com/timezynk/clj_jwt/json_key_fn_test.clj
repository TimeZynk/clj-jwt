(ns com.timezynk.clj-jwt.json-key-fn-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.timezynk.clj-jwt.json-key-fn :refer [read-key write-key]]))

(deftest test-key
  (testing "write-key should work fine."
    (is (= "foo" (write-key :foo)))
    (is (= "\"foo\"" (write-key "foo"))))
  (testing "read-key should work fine."
    (is (= :foo (read-key "foo")))
    (is (= "foo" (read-key "\"foo\"")))))
