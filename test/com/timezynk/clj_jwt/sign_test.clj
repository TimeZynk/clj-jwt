(ns com.timezynk.clj-jwt.sign-test
  (:require
   [clojure.test :refer [deftest is testing use-fixtures]]
   [com.timezynk.clj-jwt.core-test :refer [with-bc-provider-fn]]
   [com.timezynk.clj-jwt.key :refer [private-key]]
   [com.timezynk.clj-jwt.sign :refer [get-signature-fn]]))

(deftest hmac
  (testing "HMAC"
    (let [[hs256 hs384 hs512] (map get-signature-fn [:HS256 :HS384 :HS512])
          key "foo"
          body "foo"]
      (testing "HS256"
        (is (= "CLo1fidPUoBldmx3CmOav2gJs5zP03wqMVfH9RlU2go"
               (hs256 key body))))
      (testing "HS384"
        (is (= "piXjQSLhU8VQMR__GcK-j0-B52Y3YhDbUAqkjRZ5skHGnO8bfaqF9smvE8n-6AOR"
               (hs384 key body))))
      (testing "HS512"
        (is (= "zpfRr559UfVU-WtKizOGdX7fF46Z0Tburo2T_0CzrEVsGD_JZX0eky96QYdkrTNH67E1N7Rh_6z9XnIJBCPj2g"
               (hs512 key body)))))))

(deftest rsa
  (testing "RSA"
    (let [[rs256 rs384 rs512] (map get-signature-fn [:RS256 :RS384 :RS512])
          key (private-key "resources/rsa/no_pass.key")
          body "foo"]
      (testing "RS256"
        (is (= (str "VUbrxVb4ud4Iqh8h3rBHijagwFbXyml6FkqgYl9JhauWMZReM4brJh__KlBeF"
                    "R30ZruV2_VUpFYEuSnsoO1KrscnZklUow_Z8AKWCrCSxWO1I8qyskbWyN3MBq"
                    "fQxVNEc62xrzMMpdnLq6OpIk--Sh5ZdUYl-tT3wy4HV_sxQUU")
               (rs256 key body))))
      (testing "RS384"
        (is (= (str "F1HhYSk8cFdnr1ODDv-Q6YvTpMq3p8STD3lh6gingp1U5gpYmnbMqgOr_YM5z"
                    "jeUsFI1d1FolwfaeKeBRxVo9tjawb-TxFAFIdVLfZpwb3kR7nHq9NsQHfkDf_"
                    "DnfSPOi8d7wX8Eunb-padnM9sn1L4g1GYH9ReuoYhV8JUsJZE")
               (rs384 key body))))
      (testing "RS512"
        (is (= (str "VVfaoXP5WUGNSggUE1FVYV-JKZRGnFkm2ATFm2MQ7bZbyan4EBzVPUN1B5Be3"
                    "A-Z1j3LeLKFWhryRRAjzW--Ut5rs5t0MjJ4OgUUhXAEXXAeJfbeEVxzBv4C-F"
                    "e9avjnNjUgcPlJgQAMQbrLirSo8Z8hb1Iqz9f7pUuNLTkAQJA")
               (rs512 key body)))))))

(use-fixtures :once with-bc-provider-fn)

(deftest ec
  (testing "EC"
    (let [[es256 es384 es512] (map get-signature-fn [:ES256 :ES384 :ES512])
          key (private-key "resources/ec/private.key")
          body "foo"]
      (testing "ES256"
        (is (string? (es256 key body))))
      (testing "ES384"
        (is (string? (es384 key body))))
      (testing "ES512"
        (is (string? (es512 key body)))))))
