package org.cg.taylorpi

import akka.actor.{Actor, ActorRef}
import org.cg.{Message, Result}

case class IterateApproximation(n: Int, nmax:Int, acc:Float) extends Message

class Calculator(resultListener: ActorRef) extends Actor {

  /*
        4   4   4   4
        - + - + - + - ...
       -3   5  -7   9
  */
  val four = 4.toFloat

  def receive: PartialFunction[Any, Unit] = {
    case IterateApproximation(n, nmax, acc) => {
      if (n == nmax + 1)
        resultListener ! Result(acc)
      else {
        val divisor = 2 * n + 1
        val sign = if (n % 2 == 0) 1 else -1
        val term = sign * (four / divisor)
        val accNext = acc + term
        val approx = 4 + acc + term
        // println(f"approximation at step $n: $approx%1.5f divisor:$divisor sign:$sign term:$term acc:$accNext")
        self ! IterateApproximation(n + 1, nmax, accNext)
      }
    }

    case _ => println(self.path + "Not valid")
  }

}