package com.propertyupdates.app

import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scalikejdbc.ConnectionPool
import scala.concurrent.duration._
import scala.concurrent.Await

object Server
  extends App
  with Routes
{
  val config = ConfigFactory.load()
  val appName = config.getString("app.name")
  val serverHost = config.getString("server.host")
  val serverPort = config.getInt("server.port")
  val dbUser = config.getString("db.user")
  val dbPass = config.getString("db.pass")
  val dbHost = config.getString("db.host")
  val dbPort = config.getInt("db.port")
  val dbName = config.getString("db.name")
  val timeoutSeconds = config.getInt("http.timeoutSeconds")

  implicit val system: ActorSystem = ActorSystem("PropertyUpdatesSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val timeout: Timeout = Timeout(timeoutSeconds.seconds)

  ConnectionPool.singleton(
    s"jdbc:mysql://$dbHost/$dbName?autoReconnect=true&useSSL=false", dbUser, dbPass)

  val blockingDispatcher = system.dispatchers.lookup("blocking-io-dispatcher")

  implicit val db: DAOInterface = DAO()
  implicit val userService: UserServiceInterface = UserService()
  implicit val propertyService: PropertyServiceInterface = PropertyService(userService)

  Http()
    .bindAndHandle(routes, serverHost, serverPort)
    .onComplete {
      case Success(_) => println(s"$appName server running at $serverHost:$serverPort")
      case Failure(_) => println(s"$appName server unable to bind at $serverHost:$serverPort")
    }

  Await.result(system.whenTerminated, Duration.Inf)

}
