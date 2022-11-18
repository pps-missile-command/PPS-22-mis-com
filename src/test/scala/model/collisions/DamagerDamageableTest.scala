package model.collisions

import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D

object DamagerDamageableTest:
  def apply(position: Point2D, currentLife: LifePoint, affiliation: Affiliation): Collisionable =
    class DamagerDamageableTest(val position: Point2D, val currentLife: LifePoint, val affiliation: Affiliation) extends Damageable, Damager :

      protected def hitBox: HitBox = HitBoxPoint(position)

      def initialLife: LifePoint = 3

      def takeDamage(damage: LifePoint): Damageable = new DamagerDamageableTest(position, currentLife - damage, affiliation)

      def damageInflicted: LifePoint = 1
    new DamagerDamageableTest(position, currentLife, affiliation)
