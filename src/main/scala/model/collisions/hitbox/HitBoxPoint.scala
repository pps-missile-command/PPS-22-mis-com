package model.collisions.hitbox

import model.collisions.HitBox
import model.elements2d.Point2D
import model.elements2d.Point2D.GivenEquality.given
import org.scalactic.TripleEquals._
import model.collisions._
import org.scalactic.Equality

/**
 * Factory for an hit box that is a point.
 */
object HitBoxPoint:

  /**
   * Returns a new hit box that contains only a point.
   *
   * @param point the point in the hit box
   * @return a new hit box that contains only a point
   */
  def apply(point: Point2D): HitBox = HitBoxPoint(point)

  private case class HitBoxPoint(point: Point2D) extends HitBox :

    override val xMax: Option[Double] = Option(point.x)

    override val yMax: Option[Double] = Option(point.y)

    override val xMin: Option[Double] = Option(point.x)

    override val yMin: Option[Double] = Option(point.y)

    override def area(using step: Distance = 0): Iterator[Point2D] = Iterator(point)

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean = point === this.point