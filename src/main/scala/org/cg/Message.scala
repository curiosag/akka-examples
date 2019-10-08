package org.cg

trait Message

case class NextFor(n: Int) extends Message

case class Result(n: Number)