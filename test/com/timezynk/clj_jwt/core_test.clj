(ns com.timezynk.clj-jwt.core-test
  (:require
   [clj-time.core :refer [date-time plus days]]
   [clojure.test :refer [deftest is testing use-fixtures]]
   [com.timezynk.clj-jwt.core :as jwt]
   [com.timezynk.clj-jwt.key :refer [private-key public-key]])
  (:import
   [java.security Security]
   [org.bouncycastle.jce.provider BouncyCastleProvider]))

(defn with-bc-provider-fn [f]
  (try
    (Security/insertProviderAt (BouncyCastleProvider.) 1)
    (f)
    (finally
      (java.security.Security/removeProvider "BC"))))

(def claim {:iss "foo"})
(def rsa-prv-key     (private-key "resources/rsa/no_pass.key"))
(def rsa-pub-key     (public-key  "resources/rsa/no_pass.pub.key"))
(def rsa-enc-prv-key (private-key "resources/rsa/3des.key" "pass phrase"))
(def rsa-enc-pub-key (public-key  "resources/rsa/3des.pub.key"))
(def rsa-dmy-key     (public-key  "resources/rsa/dummy.key"))

(def ec-prv-key      (with-bc-provider-fn #(private-key "resources/ec/private.key")))
(def ec-pub-key      (with-bc-provider-fn #(public-key  "resources/ec/public.key")))
(def ec-dmy-key      (with-bc-provider-fn #(public-key  "resources/ec/dummy.key")))

(deftest jwt-tokenize
  (testing "JWT tokenize"
    (testing "Plain JWT should be generated."
      (is (= "eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJpc3MiOiJmb28ifQ."
             (-> claim jwt/jwt jwt/to-str))))
    (testing "If unknown algorithm is specified, exception is throwed."
      (is (thrown? Exception (-> claim jwt/jwt (jwt/sign :DUMMY "foo")))))
    (testing "HS256 signed JWT should be generated."
      (is (= (str "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.8yUIo-xkh537lD_CZycqS1zB"
                  "NhBNkIrcfzaFgwt8zdg")
             (-> claim jwt/jwt (jwt/sign "foo") jwt/to-str)))
      (is (= (str "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.8yUIo-xkh537lD_CZycqS1zB"
                  "NhBNkIrcfzaFgwt8zdg")
             (-> claim jwt/jwt (jwt/sign :HS256 "foo") jwt/to-str))))
    (testing "HS384 signed JWT should be generated."
      (is (= (str "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.34ZaTLCZGBAfcCryhYaFYy8Z"
                  "-47do1cftq365YmvIcubonhGdRnvpgV8s_iG_lvd")
             (-> claim jwt/jwt (jwt/sign :HS384 "foo") jwt/to-str))))
    (testing "HS512 signed JWT should be generated."
      (is (= (str "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.58Q4HxaxKAZIffEyDI2eRM_2"
                  "L7mK7NlNwOq8v96gbfZLMM7r2hxXKuwvMLez2XivUUCEyoaVB1Yz3vGtwAvSZQ")
             (-> claim jwt/jwt (jwt/sign :HS512 "foo") jwt/to-str))))
    (testing "RS256 signed JWT should be generated."
      (is (= (str "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.ZIjAlGryslu1APkgY1eCmaK7"
                  "GDINiGX-htlD1-33F4VXK8lUXbdm1n9F1fpHcOFksScniWMvC5f9520jdxyb5c-9CmXz21iDtFdFKWGG"
                  "zlT_hPjZ0Ta_M8goReBO0L-nDM5hJHxzEqgSZQ7tkcJ18PCdxeMia5NMRV0shGMMUzU")
             (-> claim jwt/jwt (jwt/sign :RS256 rsa-prv-key) jwt/to-str)))
      (is (= (str "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.E20DLUOR5VeoTKtH5FjR71rm"
                  "_rZV2AdXYDQCxqHpMWyZSO6wO4g67phTD727izDxd_NjuNXd2m7Atth7tGABaMhqHLh9EUwba_0nTbw6"
                  "mc_4mWaK4KBq8LG4WErQnFAVhzGbo1aEK_J7iasuUCfnxN9fZeBBUGH_h5JgPogCPdA")
             (-> claim jwt/jwt (jwt/sign :RS256 rsa-enc-prv-key) jwt/to-str))))
    (testing "RS384 signed JWT should be generated."
      (is (= (str "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.sWyMCwJhztfOcSoxRRiCAioB"
                  "H5F8WFJs5t8DxPV0D7JvB9JwaN8reIQ7kFKJiQWFbhrC7tnlT5UDX9z3fyLjdmNvLTSOII3J9UPpidE1"
                  "4WvqnXk5DV8k4QxTdWHRufssDFZe7Bsq5yBRAGZos2e8U9hOuqxCib7EjGCe09PdDhg")
             (-> claim jwt/jwt (jwt/sign :RS384 rsa-prv-key) jwt/to-str)))
      (is (= (str "eyJhbGciOiJSUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.Uz3ZGXNuuKYsmoslBrNJnVKC"
                  "GW-dptW-eOWPrTGVN1P54bgjS6QbhwE-PPL2HHGUIYlebVmHb2RKLLvmQ8y63NZ1QSXEk8QBz5-bwy6Y"
                  "m_QCYh4tfvZYheH97zHcLF3GDLlfrodukO9gGc1xpiXJiZMtIso6sGACHmXNn4LA1bk")
             (-> claim jwt/jwt (jwt/sign :RS384 rsa-enc-prv-key) jwt/to-str))))
    (testing "RS512 signed JWT should be generated."
      (is (= (str "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.QKK5oOrVU5e0eG0nt7a_3Hzw"
                  "v1YJIp1F3iSKVgbdjWyp6rhyS4O4HEql6UxUOVDvf_aTrO4NG81dIo_wzjI1LBNCVtwKhR-8KUFs4Yg3"
                  "1NLwBMazIzxX_IfkpIkUPuyDGrca7pksJ9dppte33mMK3MDv0RQQqgXiJpbLRGWSNrs")
             (-> claim jwt/jwt (jwt/sign :RS512 rsa-prv-key) jwt/to-str)))
      (is (= (str "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb28ifQ.P6ER78xL8AlV4BXrtTtBIcsc"
                  "JOktKH03Uj12mqjiS6o1h4Cf7QHKXjWxe33hrEgkzcYBHDqw7wH915f6ZnB5mkvDtBkLinA9gK0M2rfB"
                  "7NqAbxXYMDXti2PhV9PgRzOp97zPCSD98bML0Cy89E8sPcnM7-07wWOK4yhuoTWyV_8")
             (-> claim jwt/jwt (jwt/sign :RS512 rsa-enc-prv-key) jwt/to-str))))
    (testing "'exp', 'nbf', 'iat' claims should be converted as IntDate."
      (let [d     (date-time 2000 1 2 3 4 5)
            claim (merge claim {:exp (plus d (days 1)) :nbf d :iat d :dmy d})
            token (jwt/jwt claim)]
        (is (= 946868645 (-> token :claims :exp)))
        (is (= 946782245 (-> token :claims :nbf)))
        (is (= 946782245 (-> token :claims :iat)))
        (is (= d (-> token :claims :dmy)))))))

(deftest jwt-verify
  (testing "JWT verify"
    (testing "Unknown signature algorithm should be thrown exception."
      (is (thrown? Exception (jwt/verify (jwt/->JWT {:typ "JWT" :alg "DUMMY"} claim "" ""))))
      (is (thrown? Exception (jwt/verify (jwt/->JWT {:typ "JWT" :alg "DUMMY"} claim "" "") ""))))
    (testing "Plain JWT should be verified."
      (is (-> claim jwt/jwt jwt/verify))
      (is (-> claim jwt/jwt (jwt/verify "")))
      (is (-> claim jwt/jwt (jwt/verify :none "")))
      (is (-> claim jwt/jwt jwt/to-str jwt/str->jwt jwt/verify))
      (is (not (-> claim jwt/jwt jwt/to-str jwt/str->jwt (jwt/verify "foo"))))
      (is (not (-> claim jwt/jwt jwt/to-str jwt/str->jwt (jwt/verify :HS256 ""))))
      (is (not (-> claim jwt/jwt (assoc :signature "foo") jwt/verify))))
    (testing "HS256 signed JWT should be verified."
      (is (-> claim jwt/jwt (jwt/sign "foo") (jwt/verify "foo")))
      (is (-> claim jwt/jwt (jwt/sign "foo") (jwt/verify :HS256 "foo")))
      (is (not (-> claim jwt/jwt (jwt/sign "foo") (jwt/verify :HS384 "foo"))))
      (is (-> claim jwt/jwt (jwt/sign "foo") jwt/to-str jwt/str->jwt (jwt/verify "foo")))
      (is (not (-> claim jwt/jwt (jwt/sign "foo") (jwt/verify "bar")))))
    (testing "HS384 signed JWT should be verified."
      (is (-> claim jwt/jwt (jwt/sign :HS384 "foo") (jwt/verify "foo")))
      (is (-> claim jwt/jwt (jwt/sign :HS384 "foo") (jwt/verify :HS384 "foo")))
      (is (not (-> claim jwt/jwt (jwt/sign :HS384 "foo") (jwt/verify :HS256 "foo"))))
      (is (-> claim jwt/jwt (jwt/sign :HS384 "foo") jwt/to-str jwt/str->jwt (jwt/verify "foo")))
      (is (not (-> claim jwt/jwt (jwt/sign :HS384 "foo") (jwt/verify "bar")))))
    (testing "HS512 signed JWT should be verified."
      (is (-> claim jwt/jwt (jwt/sign :HS512 "foo") (jwt/verify "foo")))
      (is (-> claim jwt/jwt (jwt/sign :HS512 "foo") (jwt/verify :HS512 "foo")))
      (is (not (-> claim jwt/jwt (jwt/sign :HS512 "foo") (jwt/verify :HS256 "foo"))))
      (is (-> claim jwt/jwt (jwt/sign :HS512 "foo") jwt/to-str jwt/str->jwt (jwt/verify "foo")))
      (is (not (-> claim jwt/jwt (jwt/sign :HS512 "foo") (jwt/verify "bar")))))
    (testing "RS256 signed JWT should be verified."
      (is (-> claim jwt/jwt (jwt/sign :RS256 rsa-prv-key) (jwt/verify rsa-pub-key)))
      (is (-> claim jwt/jwt (jwt/sign :RS256 rsa-prv-key) (jwt/verify :RS256 rsa-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS256 rsa-prv-key) (jwt/verify :RS384 rsa-pub-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS256 rsa-prv-key) jwt/to-str jwt/str->jwt (jwt/verify rsa-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS256 rsa-prv-key) (jwt/verify rsa-dmy-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS256 rsa-enc-prv-key) (jwt/verify rsa-enc-pub-key)))
      (is (-> claim jwt/jwt (jwt/sign :RS256 rsa-enc-prv-key) (jwt/verify :RS256 rsa-enc-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS256 rsa-enc-prv-key) (jwt/verify :RS384 rsa-enc-pub-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS256 rsa-enc-prv-key) jwt/to-str jwt/str->jwt (jwt/verify rsa-enc-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS256 rsa-enc-prv-key) (jwt/verify rsa-dmy-key)))))
    (testing "RS384 signed JWT should be verified."
      (is (-> claim jwt/jwt (jwt/sign :RS384 rsa-prv-key) (jwt/verify rsa-pub-key)))
      (is (-> claim jwt/jwt (jwt/sign :RS384 rsa-prv-key) (jwt/verify :RS384 rsa-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS384 rsa-prv-key) (jwt/verify :RS256 rsa-pub-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS384 rsa-prv-key) jwt/to-str jwt/str->jwt (jwt/verify rsa-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS384 rsa-prv-key) (jwt/verify rsa-dmy-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS384 rsa-enc-prv-key) (jwt/verify rsa-enc-pub-key)))
      (is (-> claim jwt/jwt (jwt/sign :RS384 rsa-enc-prv-key) jwt/to-str jwt/str->jwt (jwt/verify rsa-enc-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS384 rsa-enc-prv-key) (jwt/verify rsa-dmy-key)))))
    (testing "RS512 signed JWT should be verified."
      (is (-> claim jwt/jwt (jwt/sign :RS512 rsa-prv-key) (jwt/verify rsa-pub-key)))
      (is (-> claim jwt/jwt (jwt/sign :RS512 rsa-prv-key) (jwt/verify :RS512 rsa-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS512 rsa-prv-key) (jwt/verify :RS256 rsa-pub-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS512 rsa-prv-key) jwt/to-str jwt/str->jwt (jwt/verify rsa-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS512 rsa-prv-key) (jwt/verify rsa-dmy-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS512 rsa-enc-prv-key) (jwt/verify rsa-enc-pub-key)))
      (is (-> claim jwt/jwt (jwt/sign :RS512 rsa-enc-prv-key) (jwt/verify :RS512 rsa-enc-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS512 rsa-enc-prv-key) (jwt/verify :RS256 rsa-enc-pub-key))))
      (is (-> claim jwt/jwt (jwt/sign :RS512 rsa-enc-prv-key) jwt/to-str jwt/str->jwt (jwt/verify rsa-enc-pub-key)))
      (is (not (-> claim jwt/jwt (jwt/sign :RS512 rsa-enc-prv-key) (jwt/verify rsa-dmy-key)))))

    (testing "Claims containing string key should be verified"
      (let [sclaim {"a/b" "c"}
            token  (-> sclaim jwt/jwt (jwt/sign "foo"))]
        (is (jwt/verify token "foo"))
        (is (-> token jwt/to-str jwt/str->jwt (jwt/verify "foo")))
        (is (not (jwt/verify token "bar")))))))

(deftest test-str->jwt
  (testing "str->jwt function should work."
    (let [before (jwt/jwt claim)
          after  (-> before jwt/to-str jwt/str->jwt)]
      (testing "plain jwt"
        (is (= (:header after) (:header before)))
        (is (= (:claims after) (:claims before)))
        (is (= (:signature after) (:signature before)))))
    (let [claim {:iss "foo"}
          before (-> claim jwt/jwt (jwt/sign "foo"))
          after  (-> before jwt/to-str jwt/str->jwt)]
      (testing "signed jwt"
        (is (= (:header after) (:header before)))
        (is (= (:claims after) (:claims before)))
        (is (= (:signature after) (:signature before)))))
    (let [claim {"a/b" "c"}
          before (jwt/jwt claim)
          after  (-> before jwt/to-str jwt/str->jwt)]
      (testing "Claim containing string key"
        (is (= (:header after) (:header before)))
        (is (= (:claims after) (:claims before)))
        (is (= (:signature after) (:signature before)))))))

(use-fixtures :once with-bc-provider-fn)

(deftest test1
  (testing "ES256 signed JWT should be verified."
    (is (-> claim jwt/jwt (jwt/sign :ES256 ec-prv-key) (jwt/verify ec-pub-key)))
    (is (-> claim jwt/jwt (jwt/sign :ES256 ec-prv-key) (jwt/verify :ES256 ec-pub-key)))
    (is (not (-> claim jwt/jwt (jwt/sign :ES256 ec-prv-key) (jwt/verify :ES384 ec-pub-key))))
    (is (-> claim jwt/jwt (jwt/sign :ES256 ec-prv-key) jwt/to-str jwt/str->jwt (jwt/verify ec-pub-key))))
  (testing "ES384 signed JWT should be verified."
    (is (-> claim jwt/jwt (jwt/sign :ES384 ec-prv-key) (jwt/verify ec-pub-key)))
    (is (-> claim jwt/jwt (jwt/sign :ES384 ec-prv-key) (jwt/verify :ES384 ec-pub-key)))
    (is (not (-> claim jwt/jwt (jwt/sign :ES384 ec-prv-key) (jwt/verify :ES256 ec-pub-key))))
    (is (-> claim jwt/jwt (jwt/sign :ES384 ec-prv-key) jwt/to-str jwt/str->jwt (jwt/verify ec-pub-key))))
  (testing "ES512 signed JWT should be verified."
    (is (-> claim jwt/jwt (jwt/sign :ES512 ec-prv-key) (jwt/verify ec-pub-key)))
    (is (-> claim jwt/jwt (jwt/sign :ES512 ec-prv-key) (jwt/verify :ES512 ec-pub-key)))
    (is (not (-> claim jwt/jwt (jwt/sign :ES512 ec-prv-key) (jwt/verify :ES256 ec-pub-key))))
    (is (-> claim jwt/jwt (jwt/sign :ES512 ec-prv-key) jwt/to-str jwt/str->jwt (jwt/verify ec-pub-key)))))