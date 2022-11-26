package model.collisions.hitbox

import model.collisions.*
import model.elements2d.Point2D
import org.scalactic.Equality
import utilities.MathUtilities.*

/**
 * Factory for a new hit box that has the shape of a circle.
 */
object HitBoxCircular:

  /**
   * Returns a new hit box that has the shape of a circle.
   *
   * @param center the center of the circle
   * @param radius the radius of the circle
   * @return a new hit box that has the shape of a circle
   */
  def apply(center: Point2D, radius: Double): HitBox =
    radius match
      case 0 => HitBoxPoint(center)
      case _ if radius < 0 => HitBoxEmpty
      case _ => HitBoxCircular(center, radius)

  private case class HitBoxCircular(center: Point2D, radius: Double) extends HitBoxSymmetric :

    protected def max(values: Iterable[Double]): Option[Double] = Option(values.head + radius)

    protected def min(values: Iterable[Double]): Option[Double] = Option(values.head - radius)

    protected def x: Iterable[Double] = Iterable(center.x)

    protected def y: Iterable[Double] = Iterable(center.y)

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean =
      (point <-> center) <== radius
