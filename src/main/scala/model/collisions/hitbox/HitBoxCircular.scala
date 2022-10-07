package model.collisions.hitbox

import model.collisions.HitBox
import model.elements2d.Point2D
import utilities.MathUtilities.*
import model.collisions.*
import org.scalactic.Equality

/**
 * Factory for a new hit box that has the shape of a circle.
 */
object HitBoxCircular:

  /**
   * Returns a new hit box that has the shape of a circle.
   *
   * @param center the center of the circle
   * @param radius the radius of the circle
   * @param step   the step of the point to check in the circle
   * @return a new hit box that has the shape of a circle
   */
  def apply(center: Point2D, radius: Double)(using step: Step): HitBox = HitBoxCircular(center, radius)


  private case class HitBoxCircular(center: Point2D, radius: Double)(using step: Step) extends HitBox :

    override val xMax: Option[Double] = Option(center.x + radius)

    override val yMax: Option[Double] = Option(center.y + radius)

    override val xMin: Option[Double] = Option(center.x - radius)

    override val yMin: Option[Double] = Option(center.y - radius)

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean =
      (point <-> center) <== radius
