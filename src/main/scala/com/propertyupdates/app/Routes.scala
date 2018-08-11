package com.propertyupdates.app

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import scala.concurrent.Await
import Data.DataJsonSupport

trait Routes extends DataJsonSupport {

  implicit def propertyService: PropertyServiceInterface
  implicit def timeout: Timeout

  def routes: Route =
    pathPrefix("v1" / "properties" / IntNumber / "prices" / IntNumber) {
      (propertyId, price) =>
        pathEnd {
          put {
            try {
              val property =
                Await.result(propertyService.updatePropertyPrice(propertyId, price), timeout.duration)

              complete(StatusCodes.Created, property)
            }
            catch {
              case e: Exception => complete(StatusCodes.InternalServerError, e.getMessage)
            }
          }
        }
    }

}
