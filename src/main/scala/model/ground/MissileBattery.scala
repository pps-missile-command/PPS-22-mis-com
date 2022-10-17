package model.ground

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint}
import model.elements2d.*
import model.missile.{Missile, hitboxHeight}
import utilities.Constants

import java.time
import java.time.LocalDateTime

case class MissileBattery(val position: Point2D, val life: LifePoint = Constants.missileBatteryInitialLife, val lastShoot: LocalDateTime = time.LocalDateTime.now()) extends Damageable:
    private val collider: HitBox = HitBoxRectangular(Point2D(position.x + Constants.missileBatteryBaseSize/2, position.y + Constants.missileBatteryHeightSize/2),
                                                    Constants.missileBatteryBaseSize,
                                                    Constants.missileBatteryHeightSize,
                                                    Angle.Degree(0)) //collider of the object

    /**
     * @return true: If the turret is reloading and still not able to shoot
     */
    def isReadyForShoot: Boolean = time.LocalDateTime.now().isAfter(lastShoot.plusSeconds(Constants.reloadingTime))

    /**
     * @return a tuple of MissileBattery and Missile if turret is ready for shoot, None if turret is reloading
     */
    def shootRocket(endingPoint: Point2D): Option[Tuple2[MissileBattery, Missile]] =
        if this.isReadyForShoot then
            Some((MissileBattery(position),
                Missile(Constants.missileHealth,
                    Constants.missileFriendlyDamage,
                    Constants.missileFriendlySpeed,
                    Point2D(position.x,position.y+Constants.missileBatteryHeightSize), endingPoint, 0))) //If not reloading, allow shoot
        else
            None

    /**
     * @return a string containing all the valuable informations
     */
    override def toString: String = "Missile battery --> Position: x:" + position.x + " y:" + position.y + "; Ready for shoot: " + isReadyForShoot + "; Life: " + life + "\n"

    /**
     *  @return the affiliation of the object.
     */
    override def affiliation: Affiliation = Affiliation.Friendly

    /**
     *  @return the hit box of the object.
     */
    override def hitBox: HitBox = collider

    /**
     *  @return the initial health of the object.
     */
    override def initialLife: LifePoint = Constants.missileBatteryInitialLife

    /**
     *  @return the current health of the object.
     */
    override def currentLife: LifePoint = life

    /**
     * @param damage the damage that the object received.
     * @return the object with the new health.
     */
    override def takeDamage(damage: LifePoint): MissileBattery = MissileBattery(position, life - damage, lastShoot)