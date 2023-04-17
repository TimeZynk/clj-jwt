# TimeZynk/clj-jwt [![Clojars Project](https://img.shields.io/clojars/v/com.timezynk/clj-jwt.svg)](https://clojars.org/com.timezynk/clj-jwt) 

A Clojure library for JSON Web Token(JWT), forked from [https://github.com/liquidz/clj-jwt](https://github.com/liquidz/clj-jwt)

## Supporting algorithms
 * HS256, HS384, HS512
 * RS256, RS384, RS512
 * ES256, ES384, ES512

## Not supporting
 * JSON Web Encryption (JWE)

## Usage

### Leiningen
[![com.timezynk.clj-jwt](https://clojars.org/com.timezynk.clj-jwt/latest-version.svg)](https://clojars.org/com.timezynk.clj-jwt)

### Generate

```clojure
(ns foo
  (:require
   [com.timezynk.clj-jwt.core :refer [jwt sign to-str]]
   [com.timezynk.clj-jwt.key  :refer [private-key]]
   [java-time.api :refer [days plus zoned-date-time]]))

(def claim
  {:iss "foo"
   :exp (plus (zoned-date-time) (days 1))
   :iat (zoned-date-time)})

(def rsa-prv-key (private-key "rsa/private.key" "pass phrase"))
(def ec-prv-key  (private-key "ec/private.key"))

;; plain JWT
(-> claim jwt to-str)

;; HMAC256 signed JWT
(-> claim jwt (sign :HS256 "secret") to-str)

;; RSA256 signed JWT
(-> claim jwt (sign :RS256 rsa-prv-key) to-str)

;; ECDSA256 signed JWT
(-> claim jwt (sign :ES256 ec-prv-key) to-str)
```

### Verify

```clojure
(ns foo
  (:require
   [com.timezynk.clj-jwt.core :refer [jwt sign str->jwt to-str verify]]
   [com.timezynk.clj-jwt.key  :refer [private-key public-key]]
   [java-time.api :refer [days plus zoned-date-time]]))

(def claim
  {:iss "foo"
   :exp (plus (zoned-date-time) (days 1))
   :iat (zoned-date-time)})

(def rsa-prv-key (private-key "rsa/private.key" "pass phrase"))
(def rsa-pub-key (public-key  "rsa/public.key"))
(def ec-prv-key  (private-key "ec/private.key"))
(def ec-pub-key  (public-key  "ec/public.key"))

;; verify plain JWT
(let [token (-> claim jwt to-str)]
  (-> token str->jwt verify))

;; verify HMAC256 signed JWT
(let [token (-> claim jwt (sign :HS256 "secret") to-str)]
  (-> token str->jwt (verify "secret")))

;; verify RSA256 signed JWT
(let [token (-> claim jwt (sign :RS256 rsa-prv-key) to-str)]
  (-> token str->jwt (verify rsa-pub-key)))

;; verify ECDSA256 signed JWT
(let [token (-> claim jwt (sign :ES256 ec-prv-key) to-str)]
  (-> token str->jwt (verify ec-pub-key)))
```

You can specify algorithm name (OPTIONAL) for more secure verification.

```clj
(ns foo
  (:require
   [com.timezynk.clj-jwt.core :refer [jwt sign str->jwt to-str verify]]))

;; verify with specified algorithm
(let [key   "secret"
      token (-> {:foo "bar"} jwt (sign :HS256 key) to-str)]
  (-> token str->jwt (verify :HS256 key)) ;; => true
  (-> token str->jwt (verify :none key))) ;; => false
```

### Decode

```clj
(ns foo
  (:require
   [com.timezynk.clj-jwt.core :refer [jwt sign str->jwt to-str verify]]
   [java-time.api :refer [days plus zoned-date-time]]))

(def claim
  {:iss "foo"
   :exp (plus (zoned-date-time) (days 1))
   :iat (zoned-date-time)})

;; decode plain JWT
(let [token (-> claim jwt to-str)]
  (println (-> token str->jwt :claims)))

;; decode signed JWT
(let [token (-> claim jwt (sign :HS256 "secret") to-str)]
  (println (-> token str->jwt :claims)))
```
