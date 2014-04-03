package org.flagship.console

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

case class Size(width: Int, height: Int) {
  def add(s: Size): Size = Size(this.width + s.width, this.height + s.height)
}

object SizeImplicits {
  implicit def TupleToSize(t: (Int, Int)) = Size(t._1, t._2)
  implicit def SizeToTuple(s: Size) = (s.width, s.height)
}
