package model.collisions

import model.Scorable
import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D
import model.collisions._

object DamageableTest:

  def apply(position: Point2D, life: LifePoint, affiliation: Affiliation): Damageable = new DamageableTest(position, life, life, affiliation) with Scorable(1)

case class DamageableTest(position: Point2D, currentLife: LifePoint, initialLife: LifePoint, affiliation: Affiliation) extends Damageable :

  protected def hitBox: HitBox = HitBoxPoint(position)

  def takeDamage(damage: model.collisions.LifePoint): Damageable =
    this match
      case _: Scorable => new DamageableTest(position, currentLife - damage, initialLife, affiliation) with Scorable(1)
      case _ => this.copy(position, currentLife - damage)

