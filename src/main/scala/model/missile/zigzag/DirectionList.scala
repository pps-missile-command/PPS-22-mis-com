package model.missile.zigzag

import model.World
import model.elements2d.{Angle, Point2D, Vector2D}

import scala.util.Random

/**
 * This object models the creation of a list composed by a sequence of points that represents an
 * alternate path
 */
object DirectionList:
  /**
   * This factory is used to create a generic LazyList starting from a determined element, specifying the successive
   * element of each one and the condition to filter some possible elements
   * @param start The starting point to iterate
   * @param next The next element to calculate
   * @param cond The condition used to filter elements
   * @param mapping Function that maps the iterated element into a new one
   * @tparam A Generic type of the LazyList
   * @return the LazyList created
   */
  def apply[A](start: A)(next: (A) => A)(cond: (A) => Boolean)(mapping: (A) => A): LazyList[A] =
    LazyList.iterate(start)(next) filter(cond) map(mapping)

  /**
   * This method creates a list with random zigzag behavior, this means that the directions are not alternated but
   * generated randomically
   * @param from The starting point to create the path
   * @param to The destination point
   * @param step The number of direction changes to perform
   * @param Random The Random context element used to generate randomically the nth direction
   * @return the new LazyList
   */
  def randomList(from: Point2D, to: Point2D, step: Int)(using Random): LazyList[Point2D] =
    val d = (to <-> from) / step
    val v = ((to <--> from).normalize) * d
    DirectionList(from) {
      i => i --> (-v)
    } (_ != from) {
      i => i --> (-v -|- Rand(Random))
    }

  /**
   * This factory generates a LazyList with directions alternated (Right, Left, Right...), creating a zigzag path
   * between two points
   * @param from The starting point to create the path
   * @param to The destination point
   * @param step The number of direction changes to perform
   * @param magnitude The magnitude of the rotated vectors used to traslate (on the left or right) the subpoints of the path
   * @param maxWidth The max width whithin generate the path
   * @param Random The Random object used to
   * @return the new LazyList with the zigzag path
   */
  def apply(from: Point2D, to: Point2D, step: Int, magnitude: Int = defaultMagnitude, maxWidth: Double): LazyList[Point2D] =
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
    yield (point --> (-v -|- (direction, magnitude))).filterMap(maxWidth)
