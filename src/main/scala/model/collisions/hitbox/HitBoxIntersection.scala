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

  private case class HitBoxIntersection(hitBoxes: Seq[HitBox]) extends HitBoxAggregation :

    protected def optionalConfront(l: Option[Double], r: Option[Double])(confrontFunction: (Double, Double) => Double): Option[Double] =
      (l, r) match
        case (Some(valueL), Some(valueR)) => Some(confrontFunction(valueL, valueR))
        case _ => None

    protected val functionForMax: (Double, Double) => Double = math.min
    protected val functionForMin: (Double, Double) => Double = math.max

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean = hitBoxes.forall(_.contains(point))
