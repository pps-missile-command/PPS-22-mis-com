package model.missile

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, Distance, HitBox, LifePoint}
import model.elements2d.{Angle, Point2D}
import model.missile

/**
 * This trait models a missile damageable, implementing and overriding all the required methods.
 */
trait MissileDamageable(lifePoint: LifePoint, position: Point2D, finalPosition: Point2D) extends Damageable:
  /**
   *
   *  @return the hit box of the object.
   */
  override protected def hitBox: HitBox = HitBoxRectangular(position, hitboxBase, hitboxHeight, angle.getOrElse(Angle.Degree(0)))

  /**
   *
   *  @return the initial health of the object.
   */
  override def initialLife: LifePoint = missile.initialLife

  /**
   *
   *  @return the current health of the object.
   */
  override def currentLife: LifePoint = lifePoint

  /**
   * This method calculate the angle of the missile, given its final position and the current position
   * @return the angle of the missile (of its direction)
   */
  def angle: Option[Angle] = (finalPosition <--> position).direction

