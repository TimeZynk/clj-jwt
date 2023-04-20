(ns com.timezynk.clj-jwt.key-test
  (:require
   [clojure.test :refer [deftest is testing use-fixtures]]
   [com.timezynk.clj-jwt.core-test :refer [with-bc-provider-fn]]
   [com.timezynk.clj-jwt.key :refer [private-key public-key public-key-from-string]]))

(use-fixtures :once with-bc-provider-fn)

(deftest rsa
  (testing "rsa private key"
    (testing "non encrypt key"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey
             (type (private-key "resources/rsa/no_pass.key")))))
    (testing "crypted key"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey
             (type (private-key "resources/rsa/3des.key" "pass phrase")))))
    (testing "crypted key wrong pass-phrase"
      (is (thrown? org.bouncycastle.openssl.EncryptionException
                   (private-key "resources/rsa/3des.key" "wrong pass phrase")))))
  (testing "ecdsa private key"
    (testing "ecdsa key"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
             (type (private-key "resources/ec/private.key"))))))
  (testing "rsa public key"
    (testing "rsa non encrypted key"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey
             (type (public-key "resources/rsa/no_pass.key")))))
    (testing "rsa encrypted key"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey
             (type (public-key "resources/rsa/3des.key" "pass phrase")))))
    (testing "rsa encrypted key with wrong pass phrase"
      (is (thrown? org.bouncycastle.openssl.EncryptionException
                   (type (public-key "resources/rsa/3des.key" "wrong pass phrase")))))
    (testing "rsa non encrypted key from string"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey
             (-> "resources/rsa/no_pass.key" slurp public-key-from-string type))))
    (testing "rsa encrypted key from string"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey
             (-> "resources/rsa/3des.key" slurp (public-key-from-string "pass phrase") type))))
    (testing "rsa encrypted key with wrong pass phrase from string"
      (is (thrown? org.bouncycastle.openssl.EncryptionException
                   (-> "resources/rsa/3des.key" slurp (public-key-from-string "wrong pass phrase") type))))
    (testing "invalid key string"
      (is (nil? (public-key-from-string "foobar")))))
  (testing "ecdsa public key"
    (testing "ecdsa public key"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
             (type (public-key "resources/ec/public.key")))))
    (testing "ecdsa public key from string"
      (is (= org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
             (-> "resources/ec/public.key" slurp public-key-from-string type))))))
