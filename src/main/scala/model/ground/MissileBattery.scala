package model.ground

import model.collisions.{Affiliation, Collisionable, HitBox}
import model.elements2d.Point2D

import java.time
import java.time.LocalDateTime

case class MissileBattery(private var _position: Point2D) extends Collisionable:
    private val lastShoot: LocalDateTime = time.LocalDateTime.now() //get time of creation
    private val reloatingTime: Int = 3

    def getPosition: Point2D = _position
    def isReloading: Boolean = time.LocalDateTime.now().isBefore(lastShoot.plusSeconds(reloatingTime))
    def shootRocket: Option[Tuple2[MissileBattery, MissileBattery]] = //TODO finire di implementare
        if !this.isReloading then
            Some((MissileBattery(getPosition), MissileBattery(getPosition))) //If not reloading, allow shoot
        else
            None
    override def toString: String = "Missile battery --> Position: x:" + getPosition.x + " y:" + getPosition.y + "; Reloading: " + isReloading + ";"
    override def affiliation: Affiliation = Affiliation.Friendly
    override def hitBox: HitBox = null
    override def isCollidingWith(other: Collisionable)(using step: Double): Boolean = false 