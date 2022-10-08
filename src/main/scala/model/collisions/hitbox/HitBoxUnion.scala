package model.collisions.hitbox

import model.collisions.HitBox
import model.elements2d.Point2D
import model.collisions.hitbox.*
import model.collisions.*
import org.scalactic.Equality

/**
 * Factory for hit box that is the union of other hit boxes.
 */
object HitBoxUnion:

  /**
   * Creates a hit box that is the union of the given hit boxes.
   *
   * @param hitBoxes the hit boxes to be united.
   * @return the hit box that is the union of the given hit boxes.
   */
  def apply(hitBoxes: HitBox*): HitBox = hitBoxes match
    case Seq() => HitBoxEmpty
    case Seq(hitBox) => hitBox
    case _ => HitBoxUnion(hitBoxes)

  private case class HitBoxUnion(hitBoxes: Seq[HitBox]) extends HitBox :

    def optionalConfront(l: Option[Double], r: Option[Double])(function: (Double, Double) => Double): Option[Double] =
      (l, r) match
        case (Some(l), Some(r)) => Some(function(l, r))
        case (Some(l), None) => Some(l)
        case (None, Some(r)) => Some(r)
        case _ => None

    override val xMax: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.xMax)((xMax, hitBox) => optionalConfront(xMax, hitBox.xMax)(_.max(_)))

    override val yMax: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.yMax)((yMax, hitBox) => optionalConfront(yMax, hitBox.yMax)(_.max(_)))

    override val xMin: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.xMin)((xMin, hitBox) => optionalConfront(xMin, hitBox.xMin)(_.min(_)))

    override val yMin: Option[Double] = hitBoxes.foldLeft(hitBoxes.head.yMin)((yMin, hitBox) => optionalConfront(yMin, hitBox.yMin)(_.min(_)))

    override def contains(point: Point2D)(using equality: Equality[Double]): Boolean = hitBoxes.exists(_.contains(point))

    override def area(using step: Distance): Iterator[Point2D] = hitBoxes.flatMap(_.area).distinct.iterator
