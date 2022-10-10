package model.collisions.hitbox

import model.collisions.HitBox
import model.elements2d.Point2D
import model.collisions.hitbox._
import model.collisions._

/**
 * Trait that allows to generalize the operations for an hit box created by other hit boxes
 */
trait HitBoxAggregation extends HitBox :

  /**
   * The list of hit boxes that compose this hit box
   */
  protected val hitBoxes: Seq[HitBox]

  /**
   * function that allow to return the value of the max side of the hit box
   */
  protected val functionForMin: (Double, Double) => Double

  /**
   * function that allow to return the value of the min side of the hit box
   */
  protected val functionForMax: (Double, Double) => Double

  private def hitBoxesInterval(getValue: HitBox => Option[Double], confrontFunction: (Double, Double) => Double): Option[Double] =
    hitBoxes.foldLeft(getValue(hitBoxes.head))((value, hitBox) => optionalConfront(value, getValue(hitBox))(confrontFunction))

  /**
   * Return the value of the confrontation between the two optional values.
   * The function calculate the value of the confrontation between the two double value using confrontFunction
   *
   * @param l the option on left side of the confront
   * @param r the the option on right side of the confront
   * @param confrontFunction the function that allow to confront the two sides on double type
   * @return the result of the confront
   */
  protected def optionalConfront(l: Option[Double], r: Option[Double])(confrontFunction: (Double, Double) => Double): Option[Double]

  override lazy val xMax: Option[Double] = hitBoxesInterval(_.xMax, functionForMax)

  override lazy val yMax: Option[Double] = hitBoxesInterval(_.yMax, functionForMax)

  override lazy val xMin: Option[Double] = hitBoxesInterval(_.xMin, functionForMin)

  override lazy val yMin: Option[Double] = hitBoxesInterval(_.yMin, functionForMin)

