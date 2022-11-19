package model.missile.zigzag

import model.World
import model.elements2d.{Angle, Point2D, Vector2D}

import scala.util.Random

/**
 * tot step da punto di partenza a dest
 * mi muovo sulla direzione e scelgo sotto punti
 */

object DirectionList:

  def apply[A](start: A)(next: (A) => A)(cond: (A) => Boolean)(mapping: (A) => A)(using Random): LazyList[A] =
    LazyList.iterate(start)(next) filter(cond) map(mapping)

  def randomList(from: Point2D, to: Point2D, step: Int)(using Random): LazyList[Point2D] =
    val d = (to <-> from) / step
    val v = ((to <--> from).normalize) * d
    DirectionList(from) {
      i => i --> (-v)
    } (_ != from) {
      i => i --> (-v -|- Rand(Random))
    }

  def apply(from: Point2D, to: Point2D, step: Int)(using Random): LazyList[Point2D] =
    val d = (to <-> from) / step
    val v = ((to <--> from).normalize) * d
    val stream = LazyList.iterate(0)(_ + 1)
    val pointsStream = LazyList.iterate(from)( _ --> (-v) )
    for
      index <- stream.take(step)
      if(index != 0)
      point = pointsStream.take(index + 1).toList.last
      direction = stream.take(index + 1).map {
        i => (i % 2) match
          case v if v == 0 => Right
          case _ => Left
      }.toList.last
    yield point --> (-v -|- direction)

  @main def translatePoint() =
    val p = Point2D(5,5)
    val v = Vector2D(1,1)
    val v1 = v -|- Right
    val v2 = v -|- Left
    val newP = p --> (-v1)
    val newP1 = p --> (-v2)
    println(newP)
    println(newP1)
    val p1 = p --> v
    val newP3 = p1 --> v1
    println(newP3)

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

  @main def testRandomPoints =
    given Random()
    val stream = randomList(Point2D(0, 0), Point2D(10, 10), 4)
    val points = stream.take(4).toList
    println(points map {
      i => "("+i.x+", "+i.y+")\n"
    })

  @main def testZigZag =
    given Random()
    val points = apply(Point2D(0,0), Point2D(13, 15), 3).toList
    println(points map {
      i => "("+i.x+", "+i.y+")\n"
    })