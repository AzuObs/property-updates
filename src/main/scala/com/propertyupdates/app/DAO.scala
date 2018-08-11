package com.propertyupdates.app

import scalikejdbc._
import scala.concurrent.{ExecutionContext, Future}
import Data.{PriceEuroPence, Property, User}

trait DAOInterface {

  def getPropertyWatchers(propertyId: Int): Future[List[User]]

  def updatePropertyPrice(propertyId: Int, price: PriceEuroPence): Future[Property]

}

case class DAO(implicit ec: ExecutionContext) extends DAOInterface {

  def getPropertyWatchers(propertyId: Int): Future[List[User]] =
    Future {
      DB.readOnly { implicit session =>
        sql"""
          |SELECT
          |   u.`user_id` AS userId,
          |   u.`firstname` AS firstName,
          |   u.`lastname` AS lastName,
          |   u.`email` AS email
          | FROM
          |   users u
          |   INNER JOIN properties_watchers pw ON u.`user_id` = pw.`user_id`
          | WHERE
          |   pw.`property_id` = $propertyId
          """
          .stripMargin
          .map(toUser)
          .list
          .apply()
      }
    }

  def updatePropertyPrice(propertyId: Int, price: PriceEuroPence): Future[Property] =
    Future {
      DB.localTx { implicit session =>
        sql"""
          | UPDATE
          |   properties
          | SET
          |   `price_euro_pence` = $price
          | WHERE
          |   `property_id` = $propertyId
         """
        .stripMargin
        .update()
        .apply()
      }

      DB.readOnly { implicit session =>
        sql"""
           |SELECT
           |   `property_id` AS propertyId,
           |   `address` AS address,
           |   `price_euro_pence` AS priceEuroPence
           | FROM
           |   properties
           | WHERE
           |   `property_id` = $propertyId
          """
          .stripMargin
          .map(toProperty)
          .single()
          .apply()
      } match {
        case Some(property) => property
        case None => throw new Exception("Tried to update property which does not exist")
      }
    }

  private def toUser(rs: WrappedResultSet): User =
    User(
      userId = rs.int("userId"),
      firstName = rs.string("lastName"),
      lastName = rs.string("firstName"),
      email = rs.string("email")
    )

  private def toProperty(rs: WrappedResultSet): Property =
    Property(
      propertyId = rs.int("propertyId"),
      priceEuroPence = rs.int("priceEuroPence"),
      address = rs.string("address")
    )

}
