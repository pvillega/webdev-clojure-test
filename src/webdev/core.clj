(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))

; handlers - given a Ring request returns a response
(defn greet [req]
  (cond
    (= "/" (:uri req))
    {:status  200
     :body    "Greet - Hello, World!"
     :headers {}}
    (= "/goodbye" (:uri req))
    {:status  200
     :body    "Goodbye, World!"
     :headers {}}
    :else
    {:status 404}))

(defn homepage [req]
  {:status  200
   :body    "Greet - Hello, World!"
   :headers {}})

(defn goodbye [req]
  {:status  200
   :body    "Goodbye, World!"
   :headers {}})

; middleware - can be stacked (return other middleware) and returns a handler at the end of the chain
;(defn mw [hdlr & options]
;  (fn [req]
;    (hdlr (modify req))))

; compojure route definition - depends on handlers to define behaviour
(defroutes app
           (GET "/" [] homepage)
           (GET "/goodbye" [] goodbye)
           (not-found "Page not found."))

; map main to jetty to launch server on given port
;(defn -main [port]
;  (jetty/run-jetty greet {:port (Integer. port)}))

;(defn -dev-main [port]
;  (jetty/run-jetty (wrap-reload #'greet) {:port (Integer. port)}))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
