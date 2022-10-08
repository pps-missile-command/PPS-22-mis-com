package model.missile

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, Distance, HitBox, LifePoint}
import model.elements2d.Point2D

trait MissileDamageable(lifePoint: LifePoint, position: Point2D, finalPosition: Point2D) extends Damageable:

  override protected def hitBox: HitBox = basicHitBox(position, (finalPosition <--> position).direction)

  override def initialLife: LifePoint = initialLife

  override def currentLife: LifePoint = lifePoint

