package model.collisions.hitbox

import model.collisions.HitBox

/**
 * Trait that allows to define a symmetric operation for both x and y
 */
trait HitBoxSymmetric extends HitBox :

  /**
   * Return all the x coordinates that are needed to calculate the hit box interval
   *
   * @return all the x coordinates that are needed to calculate the hit box interval
   */
  protected def x: Iterable[Double]

  /**
   * Return all the y coordinates that are needed to calculate the hit box interval
   *
   * @return all the y coordinates that are needed to calculate the hit box interval
   */
  protected def y: Iterable[Double]

  /**
   * Return the calculated max side of hit box from the values
   *
   * @param values the values to calculate the max side of hit box
   * @return An Option with the max side of hit box
   */
  protected def max(values: Iterable[Double]): Option[Double]

  /**
   * Return the calculated min side of hit box from the values
   *
   * @param values the values to calculate the min side of hit box
   * @return An Option with the min side of hit box
   */
  protected def min(values: Iterable[Double]): Option[Double]

  override lazy val xMax: Option[Double] = max(x)

  override lazy val yMax: Option[Double] = max(y)

  override lazy val xMin: Option[Double] = min(x)

  override lazy val yMin: Option[Double] = min(y)
