import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Promises extends App {

  val p = promise[String]
  val f = p.future

  println("producer")
  val producer  = future {
    println("producer ante")
    Thread.sleep(2000)

    p success "da"
//    p failure new RuntimeException("nu")

    println("producer post")
    Thread.sleep(2000)
  }

  println("consumer")
  val consumer = future {
    Thread.sleep(7000)
    f onComplete {
      case Success(r) => println(r)
      case Failure(e) => println(e.getMessage)
    }
  }

  println("gata")
  Thread.sleep(15000)
}
