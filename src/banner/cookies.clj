(ns banner.cookies
  (:require [noir.cookies :as c]))

(defn remember-me
  "Obtem o username no cookie remember-me."
  ([]
  (c/get :remember-me))
  ([username]
   "Seta o cookie remember-me para o browser do usuario browser com o
    username."
   (if username
     (c/put! :remember-me {:value username :path "/" :max-age (* 60 60 24 365)})
     (c/put! :remember-me {:value "" :path "/" :max-age -1}))))
