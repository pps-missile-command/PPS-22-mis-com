package model.collisions

import model.elements2d.Point2D
import org.scalactic.Equality

import math.BigDecimal.double2bigDecimal

/**
 * Trait that represents an hit box of an object.
 * An hit box is a shape that represents the area of an object.
 *
 */
trait HitBox:
  /**
   * Returns the grater x value of the hit box.
   *
   * @return the grater x value of the hit box if present else an Option empty.
   */
  def xMax: Option[Double]

  /**
   * Returns the greater y value of the hit box.
   *
   * @return the grater y value of the hit box if present else an Option empty .
   */
  def yMax: Option[Double]

  /**
   * Returns the lower x value of the hit box.
   *
   * @return the lower x value of the hit box if present else an Option empty.
   */
  def xMin: Option[Double]

  /**
   * Returns the lower y value of the hit box.
   *
   * @return the lower y value of the hit box if present else an Option empty.
   */
  def yMin: Option[Double]

  /**
   * Returns an iterator of points in the hit box area with distance.
   *
   * @param distance the distance between two points in the area.
   * @return an iterator of points in the hit box area with distance.
   */
  def area(using distance: Distance): Iterator[Point2D] =
    if (xMax.isEmpty || yMax.isEmpty || xMin.isEmpty || yMin.isEmpty) Iterator.empty
    else
      (for
        x <- xMin.get to xMax.get by distance
        y <- yMin.get to yMax.get by distance
        point = Point2D(x.doubleValue, y.doubleValue)
        if contains(point)
      yield
        point
        ).iterator

  /**
   * Returns true if the point is contained in the hit box.
   *
   * @param point    the point to check
   * @param equality the given equality
   * @return true if the point is contained in the hit box.
   */
  def contains(point: Point2D)(using equality: Equality[Double]): Boolean
