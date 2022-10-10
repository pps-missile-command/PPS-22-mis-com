package model.collisions

import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D

object DamagerDamageableTest:
  def apply(position: Point2D, currentLife: LifePoint, affiliation: Affiliation): Collisionable =
    case class DamagerDamageableTest(position: Point2D, currentLife: LifePoint, affiliation: Affiliation) extends Damageable, Damager :

      protected def hitBox: HitBox = HitBoxPoint(position)

      def initialLife: LifePoint = 3

      def takeDamage(damage: LifePoint): Damageable = this.copy(position, currentLife - damage)

      def damageInflicted: LifePoint = 1
    DamagerDamageableTest(position, currentLife, affiliation)
