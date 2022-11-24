package model.missile

import model.elements2d.{Angle, Point2D, Vector2D}
import model.missile.zigzag.Direction

import scala.util.Random

package object zigzag:
  /**
   * Case object and class to model the change of direction: Right, Left and a Random one (between right and left)
   */
  sealed class Direction
  case object Right extends Direction
  case object Left extends Direction
  case class Rand(rand: Random) extends Direction

  val signThreshold = 0.5
  val defaultMagnitude = 30

  /**
   * Extension method to model a map function that given a double number returns 1 or -1 whether the number is
   * greater than a threshold or not
   */
  extension(n: Double)
    def mapToSign = n match
      case v if v > signThreshold => 1
      case _ => -1

  /**
   * Extension method that define a 90 degree rotation of a given [[Vector2D]], taking the direction ([[Left]], [[Right]] or [[Rand]])
   * and return the vector rotated by 90° respectively to the left, to the right or a random one between the two types
   */
  extension(v: Vector2D)
    def -|-(d: Direction, magnitude: Int = defaultMagnitude): Vector2D = d match
      case Right => Vector2D(magnitude, Angle.Degree(v.direction.get.degree + 90))
      case Left => Vector2D(magnitude, Angle.Degree(v.direction.get.degree - 90))
      case Rand(rand) =>
        val value = rand.nextDouble()
        Vector2D(1, Angle.Degree(v.direction.get.degree + 90 * value.mapToSign))

  /**
   * Extension method that adds the pop mechanism to a LazyList, return a pair composed by the first element
   * to pop out and the rest of the list without the popped element.
   */
  extension[A](list: LazyList[A])
    def pop(): (A, LazyList[A]) = (list.take(1).head, list.tail)

  /**
   * Extension method that check if the point is within the world (between 0 and max width), and
   * adjust the x component if the point is out of bound
   */
  extension(p: Point2D)
    def ~=(p1: Point2D): Boolean =
      (Math.round(p.x * 100) / 100) == (Math.round(p1.x * 100) / 100) && (Math.round(p.y * 100) / 100) == (Math.round(p1.y * 100) / 100)

    def filterMap(maxWidth: Double): Point2D = p.x match
      case n if n > maxWidth => Point2D(maxWidth, p.y)
      case n if n < 0 => Point2D(0, p.y)
      case _ => p
