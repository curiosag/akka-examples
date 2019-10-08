package org.cg.taylorpi

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging
import org.cg.Result

object TaylorPi extends App {

  class Gateway extends Actor {
    private val log = Logging(context.system, this)
    private val startTime = now
    private val calc = ActorSystem("Pi").actorOf(Props(new Calculator(self)), "C")

    def receive: PartialFunction[Any, Unit] = {
      case Result(x) => {
        println("Result: " + (4 + x.floatValue()) + " in " + ((now - startTime) / 1000) + " secs")
        context.system.terminate()
      }
      case IterateApproximation(n, acc, nmax) => {
        calc ! IterateApproximation(n, acc,nmax)
      }
      case _ => throw new IllegalStateException()
    }

  }

  private def now = {
    System.currentTimeMillis()
  }

  ActorSystem("Pi").actorOf(Props(new Gateway), "G") ! IterateApproximation(1, 20000  , 0)
}
