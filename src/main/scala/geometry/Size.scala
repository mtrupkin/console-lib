package me.mtrupkin.geometry

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

case class Size(width: Int, height: Int) {
  def add(s: Size): Size = Size(this.width + s.width, this.height + s.height)
  def subtract(s: Size): Size = Size(this.width - s.width, this.height - s.height)

  def foreach(f: (Int, Int) => Unit) = for ( x <- 0 until width; y <- 0 until height ) f(x, y)
}

object Size {
  val ZERO = new Size(0, 0)
  val ONE = new Size(1, 1)
}

object SizeImplicits {
  implicit def TupleToSize(t: (Int, Int)) = Size(t._1, t._2)
  implicit def SizeToTuple(s: Size) = (s.width, s.height)
}
