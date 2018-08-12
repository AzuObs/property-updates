package com.propertyupdates.app

import org.scalatest._
import org.scalamock.scalatest.MockFactory
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.Timeout

import scala.concurrent.duration._
import Data._
import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.Future

class RoutesSpec
  extends FlatSpec
    with Matchers
    with MockFactory
    with ScalatestRouteTest
    with DataJsonSupport {

  "PUT v1/properties/:propertyId/prices/:price" should "return (201, Property)" in {
    new Routes {
      implicit val propertyService: PropertyServiceInterface = mock[PropertyServiceInterface]
      implicit val timeout: Timeout = Timeout(5.seconds)
      val property = Property(123, 100000, "1 Place de la Republique, 10000, France")

      propertyService.updatePropertyPrice _ expects(
        property.propertyId,
        property.priceEuroPence
      ) returns Future {
        property
      }

      Put(
        s"/v1/properties/${property.propertyId}/prices/${property.priceEuroPence}"
      ) ~> routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[Property] shouldEqual property
      }
    }
  }

}
