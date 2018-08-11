package com.propertyupdates.app

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import Data.{PriceEuroPence, Property}

trait PropertyServiceInterface {

  implicit def db: DAOInterface
  implicit def userService: UserServiceInterface

  def updatePropertyPrice(propertyId: Int, price: PriceEuroPence): Future[Property]

}

case class PropertyService(userService: UserServiceInterface)(implicit val db: DAOInterface)
  extends PropertyServiceInterface {

  def updatePropertyPrice(propertyId: Int, price: PriceEuroPence): Future[Property] =
    db
      .updatePropertyPrice(propertyId, price)
      .map { property =>
        userService.alertPropertyWatchers(property)
        property
      }

}
