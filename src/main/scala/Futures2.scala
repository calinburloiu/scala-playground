import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Futures extends App {

  val fut1 = future {
    var sum = 0
    for (i <- 1 to 5 by 2) {
      Thread.sleep(1000)
      println(i)
      sum += i
    }

    throw new RuntimeException("bleaa!")
    sum
  } recoverWith {
    case _ => future {
      Thread.sleep(5000)
      -1
    }
  }

  val fut2 = future {
    var sum = 0
    for (i <- 2 to 10 by 2) {
      Thread.sleep(1000)
      println(i)
      sum += i
    }

    throw new RuntimeException("ah!")
    sum
  } fallbackTo {
    future {
      Thread.sleep(5000)
      throw new RuntimeException("oo!")
      -2
    }
  }

  val fut3 = for {
    sum1 <- fut1
    sum2 <- fut2
  } yield (s"$sum1 $sum2")

  fut3.onComplete {
    case Success(r) => println(s"succes $r")
    case Failure(e) => println(s"esec ${e.getMessage}")
  }

  fut3.foreach { x =>
    println(x)
  }

  println("end")
  Thread.sleep(17000)
}
