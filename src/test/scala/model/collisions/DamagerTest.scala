package model.collisions

import model.collisions.hitbox.HitBoxPoint
import model.elements2d.Point2D

object DamagerTest:

  def apply(position: Point2D, affiliation: Affiliation): Damager = new DamagerTest(position, affiliation)

  private class DamagerTest(val position: Point2D,val affiliation: Affiliation) extends Damager :

    protected def hitBox: HitBox = HitBoxPoint(position)

    def damageInflicted: LifePoint = 1
  
