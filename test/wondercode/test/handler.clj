(ns wondercode.test.handler
  (:use [midje.sweet]
        [ring.mock.request]
        [wondercode.handler]))

(fact "it has a main page"
      (let [response (app (request :get "/"))]
        (:status response) => 200
        (:body response) => "Hello World"))

(fact "it has a failing route"
      (let [response (app (request :get "/invalid"))]
        (:status response) => 404))