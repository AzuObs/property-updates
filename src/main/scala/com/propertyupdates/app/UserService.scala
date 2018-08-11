package com.propertyupdates.app

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import Data.{User, Property}

trait UserServiceInterface {

  implicit def db: DAOInterface

  def alertPropertyWatchers(property: Property): Future[List[User]]

}

case class UserService(implicit val db: DAOInterface)
  extends UserServiceInterface {

  def alertPropertyWatchers(property: Property): Future[List[User]] =
    db
      .getPropertyWatchers(property.propertyId)
      .map { users =>
        users.map { user => {
          println(
            s"""
              |Mr. ${user.firstName} ${user.lastName} notified of a price change
              |for property ${property.address} with a new price of ${property.priceEuroPence}
             """.stripMargin
          )
          user
        }}}

}
