package model.missile

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, Distance, HitBox, LifePoint}
import model.elements2d.{Angle, Point2D}
import model.missile

trait MissileDamageable(lifePoint: LifePoint, position: Point2D, finalPosition: Point2D) extends Damageable:

  override protected def hitBox: HitBox = basicHitBox(position, angle)

  override def initialLife: LifePoint = missile.initialLife

  override def currentLife: LifePoint = lifePoint
  
  def angle: Option[Angle] = (finalPosition <--> position).direction

