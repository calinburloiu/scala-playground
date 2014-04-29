import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

object Futures extends App {

  def doCount(id: Int, count: Int) {
    for (i <- 1 to count) {
      Thread.sleep(1000)
      println(s"$id:\t$i")
    }
  }

  val fut1 = future {
    doCount(1, 2)
    throw new RuntimeException("exception 1")
    1
  } recoverWith {
    case _ => future {
      doCount(11, 5)
      throw new RuntimeException("error recoverWith 1")
      11
    }
  }

  val fut2 = future {
    doCount(2, 3)
    throw new RuntimeException("exception 2")
    2
  } recover {
    case _ => 22
  }

  Thread.sleep(1500)
  println("start for")
  val r = (for {
    v1 <- fut1
    v2 <- fut2
  } yield s"$v1 $v2") andThen {
    case Success(s) => println(s"succes $s")
    case Failure(e) => println(s"esec ${e.getMessage}")
  } andThen {
    case _ => {
      Thread.sleep(500)
      println("and then again")
    }
  }

//  val fut3 = future {
//    doCount(3, 3)
////    throw new RuntimeException("exception 3")
//    3
//  }
//
//  val r = (for {
//    v3 <- fut3.failed
//  } yield v3) andThen {
//    case Success(x) => println(s"succes $x")
//    case Failure(e) => println(s"esec $e")
//  }



  println("end")
  Await.ready(r, 15 seconds)
}
