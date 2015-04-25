(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

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

(defn about [req]
  {:status  200
   :body    "I'm a student of Clojure that is trying things around"
   :headers {}})

(defn yo [req]
  (let [name (get-in req [:route-params :name])]
    {:status  200
     :body    (str "Yo! " name "!")
     :headers {}}))

(def ops
      {"+" +
       "-" -
       "*" *
       ":" /})

(defn calculator [req]
  (let [a (Integer. (get-in req [:route-params :a]))
        b (Integer. (get-in req [:route-params :b]))
        op (get-in req [:route-params :op])
        f (get ops op)]
    (if f
      {:status  200
       :body    (str "Result: "  (f a b))
       :headers {}}
      {:status  404
       :body    (str "Unknown operator: " op)
       :headers {}})))

; middleware - can be stacked (return other middleware) and returns a handler at the end of the chain
;(defn mw [hdlr & options]
;  (fn [req]
;    (hdlr (modify req))))

; compojure route definition - depends on handlers to define behaviour
(defroutes app
           (GET "/" [] homepage)
           (GET "/goodbye" [] goodbye)
           (GET "/about" [] about)
           (GET "/yo/:name" [] yo)
           (GET "/calc/:a/:op/:b" [] calculator)
           (GET "/request" [] handle-dump)                  ; does a dump of the header in a nice format
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
