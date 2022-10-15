package model.collisions

import model.Scorable
import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D
import model.collisions._

object DamageableTest:

  def apply(position: Point2D, life: LifePoint, affiliation: Affiliation): Damageable = new DamageableTest(position, life, life, affiliation) with Scorable(1)

class DamageableTest(_position: Point2D, _currentLife: LifePoint, _initialLife: LifePoint, _affiliation: Affiliation) extends Damageable :

  def affiliation: Affiliation = _affiliation
  def currentLife: LifePoint = _currentLife
  def initialLife: LifePoint = _initialLife

  protected def hitBox: HitBox = HitBoxPoint(_position)

  def takeDamage(damage: LifePoint): Damageable =
    this match
      case _: Scorable => new DamageableTest(_position, currentLife - damage, initialLife, affiliation) with Scorable(1)
      case _ => new DamageableTest(_position, currentLife - damage, initialLife, affiliation)

