(ns wondercode.test.handler
  (:require [midje.sweet :refer :all]
            [ring.mock.request :refer :all]
            [wondercode.handler :refer :all]))

(fact "it has a failing route"
      (let [response (app-routes (request :get "/invalid"))]
        (:status response) => 404))