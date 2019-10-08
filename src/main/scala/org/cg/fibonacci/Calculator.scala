package org.cg.fibonacci

import akka.actor.{Actor, ActorRef, Props}

trait Message

case class Depth(n: Int) extends Message

case class Sum(n: Int)

class Calculator(caller: ActorRef) extends Actor {
  var returned: List[Integer] = Nil

  def receive: PartialFunction[Any, Unit] = {
    case Depth(x) => {
      if (x == 1 || x == 2)
        caller ! Sum(1)
      else if (x > 2) {
        newCalculator("L" + x) ! Depth(x - 1)
        newCalculator("R" + x) ! Depth(x - 2)
      }
    }

    case Sum(x) => {
      returned = x :: returned

      if (returned.size == 2) {
        val sum = returned.foldLeft(0)(_ + _)
        println(sum + " at " + self.path.toString.replace("akka://Fib/user", ""))
        caller ! Sum(sum)
      }

    }
    case _ => throw new IllegalStateException()

  }

  private def newCalculator(n: String) = {
    context.actorOf(Props(new Calculator(self)), name = n)
  }

}