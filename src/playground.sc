import scala.concurrent.ExecutionContext
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Success, Failure}

Await.result(Future{ 10 }, Duration.Inf) match {

}