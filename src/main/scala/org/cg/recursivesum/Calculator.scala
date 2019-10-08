package org.cg.recursivesum

import akka.actor.{Actor, ActorRef, Props}
import org.cg.{NextFor, Result}


class Calculator(depth: Int, caller: ActorRef) extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case NextFor(x) => {
      if (x == 0)
        caller ! Result(0)
      else {
        continueWith(x) ! NextFor(x - 1)
      }
    }

    case Result(x) => {
      caller ! Result(depth + x.intValue())
    }

    case _ => println(self.path + "Not valid")

  }

  def continueWith(x: Int): ActorRef = {
    context.actorOf(Props(new Calculator(x, self)), x.toString)
  }

}