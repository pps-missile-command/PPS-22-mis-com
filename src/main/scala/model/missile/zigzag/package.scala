package model.missile

import model.elements2d.{Angle, Point2D, Vector2D}
import model.missile.zigzag.Direction

import scala.util.Random

package object zigzag:

  sealed class Direction
  case object Right extends Direction
  case object Left extends Direction
  case class Rand(rand: Random) extends Direction

  val signThreshold = 0.5

  extension(n: Double)
    def mapToSign = n match
      case v if v > signThreshold => 1
      case _ => -1

  extension(v: Vector2D)
    def -|-(d: Direction): Vector2D = d match
      case Right => Vector2D(30, Angle.Degree(v.direction.get.degree + 90))
      case Left => Vector2D(30, Angle.Degree(v.direction.get.degree - 90))
      case Rand(rand) =>
        val value = rand.nextDouble()
        Vector2D(1, Angle.Degree(v.direction.get.degree + 90 * value.mapToSign))

  extension[A](list: LazyList[A])
    def pop(): (A, LazyList[A]) = (list.take(1).head, list.tail)
