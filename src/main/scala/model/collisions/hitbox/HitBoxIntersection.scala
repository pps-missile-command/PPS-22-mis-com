package model.collisions.hitbox

import model.collisions.HitBox
import model.elements2d.Point2D
import model.collisions.hitbox.*
import model.collisions.*
import org.scalactic.Equality

/**
 * Factory for hit box that is the intersection of other hit boxes.
 */
object HitBoxIntersection:

  /**
   * Returns the intersection of the given hit boxes, if there isn't an intersection return the hit box empty.
   *
   * @param hitBoxes the hit boxes to intersect
   * @return the intersection of the given hit boxes
   */
  def apply(hitBoxes: HitBox*)(using Distance): HitBox =
    val intersection = hitBoxes match
      case Seq() => HitBoxEmpty
      case Seq(hitBox) => hitBox
      case _ => HitBoxIntersection(hitBoxes)
    intersection match
      case HitBoxEmpty => HitBoxEmpty
      case _ if intersection.xMax.isEmpty || intersection.yMax.isEmpty || intersection.xMin.isEmpty || intersection.yMin.isEmpty => HitBoxEmpty
      case _ if intersection.xMax.get < intersection.xMin.get => HitBoxEmpty
      case _ if intersection.yMax.get < intersection.yMin.get => HitBoxEmpty
      case _ if intersection.area.isEmpty => HitBoxEmpty
      case _ => intersection

  private case class HitBoxIntersection(hitBoxes: Seq[HitBox]) extends HitBox :

    def optionalConfront(l: Option[Double], r: Option[Double])(function: (Double, Double) => Double): Option[Double] =
      (l, r) match
        case (Some(valueL), Some(valueR)) => Some(function(valueL, valueR))
        case _ => None

    override val xMax: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.xMax)((xMax, hitBox) => optionalConfront(xMax, hitBox.xMax)(_.min(_)))

    override val yMax: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.yMax)((yMax, hitBox) => optionalConfront(yMax, hitBox.yMax)(_.min(_)))

    override val xMin: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.xMin)((xMin, hitBox) => optionalConfront(xMin, hitBox.xMin)(_.max(_)))

    override val yMin: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.yMin)((yMin, hitBox) => optionalConfront(yMin, hitBox.yMin)(_.max(_)))

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean = hitBoxes.forall(_.contains(point))
