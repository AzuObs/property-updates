package com.propertyupdates.app

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

object Data {

  type PriceEuroPence = Int

  final case class User (
    userId: Int,
    firstName: String,
    lastName: String,
    email: String
  )

  final case class Property(
    propertyId: Int,
    priceEuroPence: PriceEuroPence,
    address: String,
  )

  trait DataJsonSupport extends SprayJsonSupport {
    import spray.json.DefaultJsonProtocol._

    implicit val userJsonFormat = jsonFormat4(User)
    implicit val propertyJsonFormat = jsonFormat3(Property)

  }

}
