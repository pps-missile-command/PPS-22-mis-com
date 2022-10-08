package model.missile

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, Distance, HitBox, LifePoint}
import model.elements2d.Point2D

trait MissileDamageable(lifePoint: LifePoint, position: Point2D) extends Damageable:

  override def isDestroyed: Boolean = super.isDestroyed

  override protected def hitBox: HitBox = basicHitBox(position)

  override def initialLife: LifePoint = initialLife

  override def currentLife: LifePoint = lifePoint

