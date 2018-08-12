package com.propertyupdates.app

import org.scalatest._
import org.scalamock.scalatest.MockFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import com.propertyupdates.app.Data._

class UserServiceSpec
  extends FlatSpec
    with Matchers
    with MockFactory {

  "UserService.alertPropertyWatchers" should "get property watchers from database" in {
    implicit val db: DAOInterface = mock[DAOInterface]
    val service = UserService()
    val property = Property(123, 100000, "1 Place de la Republique, 10000, France")
    val user = User(1, "Jeremy", "Corr", "jeremy.corr@gmail.com")

    db.getPropertyWatchers _ expects property.propertyId returns Future { List(user) }

    Await.result(service.alertPropertyWatchers(property), 2.seconds) should contain only user
  }

  it should "call on the notification service to alert users about property price change" in {}

}
