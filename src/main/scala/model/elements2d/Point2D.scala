package model.elements2d

import scala.annotation.targetName

case class Point2D(x: Double, y: Double):

  @targetName("translate")
  def -->(p: Vector2D): Point2D = Point2D(x + p.x, y + p.y)

  @targetName("distance")
  def <->(p: Point2D): Double = (this <--> p).magnitude

  @targetName("distance")
  def <-->(p: Point2D): Vector2D = Vector2D(p.x - x, p.y - y)

  @targetName("scale")
  def *(s: Vector2D): Point2D = Point2D(x * s.x, y * s.y)

  @targetName("scale")
  def *(s: Double): Point2D = Point2D(x * s, y * s)


