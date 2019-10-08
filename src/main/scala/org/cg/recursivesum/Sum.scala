package org.cg.recursivesum

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import akka.util.Timeout
import org.cg.{NextFor, Result}

object Sum extends App {
  var result = -1

  class Gateway extends Actor {
    private val log = Logging(context.system, this)

    def receive: PartialFunction[Any, Unit] = {
      case Result(x) => {

        result = x.intValue()
      }
      case NextFor(x) => {
        context.actorOf(Props(new Calculator(x, self)), "FibN") ! NextFor(x)
      }
      case _ => println(self.path + " Invalid request")
    }

  }

  private def await(what: () => Boolean): Unit = {
    while (!what())
      Thread.sleep(200)
  }

  private def now = {
    System.currentTimeMillis()
  }

  val system = ActorSystem("RecSum")

  private val startTime = now

  private implicit val timeout = Timeout(5, TimeUnit.SECONDS)
  val gateway: ActorRef = system.actorOf(Props(new Gateway), "G")
  gateway ! NextFor(50000)

  await(() => result > 0)


  println("Result " + result + " in " + ((now - startTime) / 1000) + " secs")
  system.terminate().foreach(i => throw new IllegalStateException())(system.getDispatcher)
}
