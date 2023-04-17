(ns com.timezynk.clj-jwt.attack-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [com.timezynk.clj-jwt.core :as jwt]
   [com.timezynk.clj-jwt.key :refer [private-key public-key]]))

(def pub-key-path "resources/rsa/no_pass.pub.key")
(def rsa-prv-key  (private-key "resources/rsa/no_pass.key"))
(def rsa-pub-key  (public-key  pub-key-path))

(deftest test-algorithm->none-attack
  (let [key "secret"
        original (-> {:foo "bar"} jwt/jwt (jwt/sign :HS256 key))
        attacked (update-in original [:header :alg] (constantly "none"))]
    (testing "attack"
      (is (jwt/verify original key))
      (is (not (jwt/verify attacked key))))

    (testing "defense"
      (is (jwt/verify original :HS256 key))
      (is (not (jwt/verify original :RS256 key)))
      (is (not (jwt/verify attacked :HS256 key))))))

(deftest test-rsa->hmac-attack
  (let [base      (jwt/jwt {:foo "bar"})
        original  (jwt/sign base :RS256 rsa-prv-key)
        hmac-sign (-> base (jwt/sign :HS256 (str/trim (slurp pub-key-path))) :signature)
        attacked  (-> original
                      (update-in [:header :alg] (constantly "HS256"))
                      (update-in [:signature] (constantly hmac-sign)))]
    (testing "attack"
      (is (jwt/verify original rsa-pub-key))
      (is (thrown? Exception (jwt/verify attacked rsa-pub-key))))

    (testing "defense"
      (is (jwt/verify original :RS256 rsa-pub-key))
      (is (not (jwt/verify original :HS256 rsa-pub-key)))
      (is (not (jwt/verify attacked :RS256 rsa-pub-key))))))
