package model.missile.zigzag

import model.World
import model.elements2d.{Angle, Point2D, Vector2D}

import scala.util.Random

/**
 * tot step da punto di partenza a dest
 * mi muovo sulla direzione e scelgo sotto punti
 */

sealed class Direction
case object Right extends Direction
case object Left extends Direction

extension(v: Vector2D)

  def |(d: Direction): Vector2D = d match
    case Right => Vector2D(1, Angle.Degree(v.direction.get.degree + 90))
    case Left => Vector2D(1, Angle.Degree(v.direction.get.degree - 90))

extension[A](list: LazyList[A])
  def pop(): (A, LazyList[A]) = (list.take(1).head, list.drop(1))

object DirectionList:

  def apply(from: Point2D, to: Point2D)(using Random): LazyList[Point2D] =
    val vector = (from <--> to).normalize
    for
      r <- LazyList.iterate(0)(_ + 1)

      x = Random.nextDouble() * World.width
      y = Random.nextDouble() * World.height
      //angolo da gestire
    yield Point2D(x, y)

  @main def translatePoint() =
    val p = Point2D(5,5)
    val v = Vector2D(1,1)
    val v1 = v | Right
    val v2 = v | Left
    val newP = p --> (-v1)
    val newP1 = p --> (-v2)
    println(newP)
    println(newP1)

  @main def testPoints() =
    val step = 4
    val p = Point2D(0,0)
    val p1 = Point2D(3,3)
    val d = (p <-> p1) / step
    val v = ((p1 <--> p).normalize) * d
    println(v)
    val newP = p --> (-v)
    println(newP)
    val newP1 = newP --> (-v)
    println(newP1)
    val newP2 = newP1 --> (-v)
    println(newP2)
    val newP3 = newP2 --> (-v)
    println(newP3)

  @main def test1() =
    val stream = LazyList.iterate[Int](0)(_ + 1)
    val firstElement = stream.take(4).toList
    val stream1 = stream.drop(4)
    println(firstElement)
    val secondElement = stream1.take(1).toList
    println(secondElement)