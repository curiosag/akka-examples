package org.cg.fibonacci

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

object Fibonacci extends App {

  class Gateway extends Actor {
    private val log = Logging(context.system, this)
    private val startTime = now

    def receive: PartialFunction[Any, Unit] = {
      case Sum(x) => {
        println(self.path + "\n\nResult " + x + " in " + ((now - startTime) / 1000) + " secs")
        context.system.terminate()
      }
      case Depth(x) => {
        context.actorOf(Props(new Calculator(self)), "FibN") ! Depth(x)
      }
      case _ => throw new IllegalStateException()
    }
  }

  private def now = {
    System.currentTimeMillis()
  }

  val system = ActorSystem("Fib")
  val gateway = system.actorOf(Props(new Gateway), "G")
  gateway ! Depth(25)
}
