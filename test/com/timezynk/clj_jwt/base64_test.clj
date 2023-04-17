(ns com.timezynk.clj-jwt.base64-test
  (:require
   [clojure.test :refer [are deftest is testing]]
   [com.timezynk.clj-jwt.base64 :refer [decode encode encode-str decode-str url-safe-decode url-safe-decode-str url-safe-encode-str]]))

(deftest base64
  (testing "base64/encode"
    (testing "string -> byte array encode"
      (is (= (Class/forName "[B")
             (class (encode "foo"))))
      (are [a b] (= a (String. (encode b)))
        "Zm9v" "foo"
        "YmFy" "bar"
        "Zm9vLmJhcg==" "foo.bar"))
    (testing "string -> string encode"
      (are [a b] (= a (encode-str b))
        "Zm9v" "foo"
        "YmFy" "bar"
        "Zm9vLmJhcg==" "foo.bar"))
    (testing "byte array -> string encode"
      (are [a b] (= a (encode-str (.getBytes b "UTF-8")))
        "Zm9v" "foo"
        "YmFy" "bar"
        "Zm9vLmJhcg==" "foo.bar"))
    (testing "byte array -> byte array encode"
      (is (= (Class/forName "[B")
             (class (encode (.getBytes "foo" "UTF-8")))))
      (are [a b] (= a (String. (encode (.getBytes b "UTF-8"))))
        "Zm9v" "foo"
        "YmFy" "bar"
        "Zm9vLmJhcg==" "foo.bar")))
  (testing "base64/decode"
    (testing "string -> byte array decode"
      (is (= (Class/forName "[B")
             (class (decode "Zm9v"))))
      (are [a b] (= a (String. (decode b)))
        "foo" "Zm9v"
        "bar" "YmFy"
        "foo.bar" "Zm9vLmJhcg=="))
    (testing "string -> string decode"
      (are [a b] (= a (decode-str b))
        "foo" "Zm9v"
        "bar" "YmFy"
        "foo.bar" "Zm9vLmJhcg=="))
    (testing "byte array -> string decode"
      (are [a b] (= a (decode-str (.getBytes b "UTF-8")))
        "foo" "Zm9v"
        "bar" "YmFy"
        "foo.bar" "Zm9vLmJhcg=="))
    (testing "byte array -> byte array decode"
      (is (= (Class/forName "[B")
             (class (decode (.getBytes "Zm9v" "UTF-8")))))
      (are [a b] (= a (String. (decode (.getBytes b "UTF-8"))))
        "foo" "Zm9v"
        "bar" "YmFy"
        "foo.bar" "Zm9vLmJhcg==")))
  (testing "base64/url-safe-encode-str"
    (testing "string -> string encode"
      (are [a b] (= a (url-safe-encode-str b))
        "Zm9v" "foo"
        "YmFy" "bar"
        "Zm9vLmJhcg" "foo.bar"))
    (testing "byte array -> string encode"
      (are [a b] (= a (url-safe-encode-str (.getBytes b "UTF-8")))
        "Zm9v" "foo"
        "YmFy" "bar"
        "Zm9vLmJhcg" "foo.bar")))
  (testing "base64/url-safe-decode"
    (testing "string -> string url-safe decode"
      (are [a b] (= a (url-safe-decode-str b))
        "foo" "Zm9v"
        "bar" "YmFy"
        "foo.bar" "Zm9vLmJhcg"))
    (testing "string -> byte array url-safe decode"
      (is (= (Class/forName "[B")
             (class (url-safe-decode "Zm9v"))))
      (are [a b] (= a (String. (url-safe-decode b)))
        "foo" "Zm9v"
        "bar" "YmFy"
        "foo.bar" "Zm9vLmJhcg"))))
