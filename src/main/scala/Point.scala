package org.flagship.console

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

case class Point(x: Int, y: Int) {
  def move(p: Point): Point = Point(x + p.x, y + p.y)
  def +(p: Point) = Point(x + p.x, y + p.y)

  import Point._
  def up() { move(Up) }
  def down() { move(Down) }
  def left() { move(Left) }
  def right() { move(Right) }
}

object PointImplicits {
  implicit def TupleToPoint(t: (Int, Int)) = Point(t._1, t._2)
  implicit def PointToTuple(p: Point) = (p.x, p.y)
}

object Point {
  val Origin: Point = Point(0, 0)
  val Up: Point = Point(0, -1)
  val Down: Point = Point(0, 1)
  val Left: Point = Point(-1, 0)
  val Right: Point = Point(1, 0)
 }
