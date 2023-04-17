(ns com.timezynk.clj-jwt.intdate-test
  (:require
   [clj-time.core   :refer [date-time]]
   [clojure.test :refer [deftest is testing]]
   [com.timezynk.clj-jwt.intdate :refer [intdate->joda-time joda-time->intdate]]))

(deftest convert-time
  (testing "joda-time->intdate should work fine."
    (let [d (date-time 2000 1 2 3 4 5)]
      (is (= 946782245 (joda-time->intdate d)))
      (is (thrown? AssertionError (joda-time->intdate nil)))))

  (testing "intdate->joda-time should work fine."
    (let [d (date-time 2000 1 2 3 4 5)
          i (joda-time->intdate d)]
      (is (= d (intdate->joda-time i)))
      (is (thrown? AssertionError (intdate->joda-time nil))))))
