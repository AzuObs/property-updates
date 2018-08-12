package com.propertyupdates.app

import org.scalatest._
import org.scalamock.scalatest.MockFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import Data._

class PropertyServiceSpec
  extends FlatSpec
    with Matchers
    with MockFactory {

  "PropertyService.updatePropertyPrice" should "update the property price in the database" in {
    implicit val db: DAOInterface = mock[DAOInterface]
    val userService = UserService()
    val propertyService = PropertyService(userService)
    val property = Property(123, 100000, "1 Place de la Republique, 10000, France")

    db.updatePropertyPrice _ expects (
      property.propertyId,
      property.priceEuroPence
    ) returns Future {
      property
    }

    propertyService.updatePropertyPrice(property.propertyId, property.priceEuroPence)
  }

  it should "call on the user service to alert users" in {
    implicit val db: DAOInterface = mock[DAOInterface]
    val userService = mock[UserService]
    val propertyService = PropertyService(userService)
    val property = Property(123, 100000, "1 Place de la Republique, 10000, France")

    db.updatePropertyPrice _ expects(
      property.propertyId,
      property.priceEuroPence
    ) returns Future {
      property
    }
    userService.alertPropertyWatchers _ expects property returns Future {
      List.empty[User]
    }

    Await.result(
      propertyService.updatePropertyPrice(property.propertyId, property.priceEuroPence),
      2.seconds
    )
  }

  it should "return a property" in {
    implicit val db: DAOInterface = mock[DAOInterface]
    val userService = mock[UserService]
    val propertyService = PropertyService(userService)
    val property = Property(123, 100000, "1 Place de la Republique, 10000, France")

    db.updatePropertyPrice _ expects(
      property.propertyId,
      property.priceEuroPence
    ) returns Future {
      property
    }
    userService.alertPropertyWatchers _ expects property returns Future {
      List.empty[User]
    }

    Await.result(
      propertyService.updatePropertyPrice(property.propertyId, property.priceEuroPence),
      2.seconds
    ) shouldBe property
  }

}
