package model.ground

import model.collisions.{Affiliation, Collisionable, Damageable, HitBox}
import model.elements2d.Point2D

case class City(private var _position: Point2D, private var _life: Int = 3) extends Damageable:
    def getPosition: Point2D = _position
    override def initialLife: Double = 3
    override def currentLife: Double = _life
    override def takeDamage(damage: Int): Damageable = City(_position, _life - damage)
    override def affiliation: Affiliation = Affiliation.Friendly
    override def hitBox: HitBox = null
    override def isCollidingWith(other: Collisionable)(using step: Double): Boolean = false