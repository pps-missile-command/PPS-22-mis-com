package model.collisions

import model.elements2d.Point2D
import math.BigDecimal.double2bigDecimal

/**
 * Trait that represents an hit box of an object.
 * An hit box is a shape that represents the area of an object.
 *
 * @param step the distance of the point in the shape for the iterator
 */
trait HitBox(using step: Double) extends Iterable[Point2D] :
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
   * Returns an iterator of points in the hit box with distance step.
   *
   * @return an iterator of points in the hit box with distance step.
   */
  override def iterator: Iterator[Point2D] =
    if (xMax.isEmpty || yMax.isEmpty || xMin.isEmpty || yMin.isEmpty) Iterator.empty
    else
      (for
        x <- xMin.get to xMax.get by step
        y <- yMin.get to yMax.get by step
        point = Point2D(x.doubleValue, y.doubleValue)
        if contains(point)
      yield
        point
        ).iterator

  /**
   * Returns true if the point is contained in the hit box.
   *
   * @param point the point to check
   * @return true if the point is contained in the hit box.
   */
  def contains(point: Point2D): Boolean
