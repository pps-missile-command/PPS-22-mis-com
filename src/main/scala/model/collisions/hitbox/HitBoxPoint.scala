package model.collisions.hitbox

import model.collisions.*
import model.collisions.hitbox.HitBoxSymmetric
import model.elements2d.Point2D
import model.elements2d.Point2D.GivenEquality.given
import org.scalactic.Equality
import org.scalactic.TripleEquals.*

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

  private case class HitBoxPoint(point: Point2D) extends HitBoxSymmetric :

    protected def max(values: Iterable[Double]): Option[Double] = Option(values.head)

    protected def min(values: Iterable[Double]): Option[Double] = max(values)

    protected val x: Iterable[Double] = Iterable(point.x)

    protected val y: Iterable[Double] = Iterable(point.y)

    override def area(using step: Distance = 0): Iterator[Point2D] = Iterator(point)

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean = point === this.point