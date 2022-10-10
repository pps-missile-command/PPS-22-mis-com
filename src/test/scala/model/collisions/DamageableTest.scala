package model.collisions

import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D

object DamageableTest:

  import model.collisions._

  def apply(position: Point2D, life: LifePoint, affiliation: Affiliation): Damageable =
    case class DamageableTest(position: Point2D, currentLife: LifePoint, affiliation: Affiliation) extends Damageable :

      protected def hitBox: HitBox = HitBoxPoint(position)

      def initialLife: LifePoint = life

      def takeDamage(damage: model.collisions.LifePoint): Damageable = this.copy(position, currentLife - damage)
    DamageableTest(position, life, affiliation)