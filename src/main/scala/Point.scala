package org.flagship.console

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

case class Point(x: Int, y: Int) {
  def move(p: Point): Point = {
    Point(x + p.x, y + p.y)
  }
}

object PointImplicits {
  implicit def TupleToPoint(t: (Int, Int)) = new Point(t._1, t._2)
  implicit def PointToTuple(p: Point) = (p.x, p.y)
}

object Point {
  val Origin: Point = new Point(0, 0)
  val Up: Point = new Point(0, -1)
  val Down: Point = new Point(0, 1)
  val Left: Point = new Point(-1, 0)
  val Right: Point = new Point(1, 0)
 }
