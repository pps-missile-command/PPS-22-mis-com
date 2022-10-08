package model.ground

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint}
import model.elements2d._
import utilities.Constants

import java.time
import java.time.LocalDateTime

case class MissileBattery(private var _position: Point2D, private var _life: LifePoint = Constants.missileBatteryInitialLife, private var _lastShoot: LocalDateTime = time.LocalDateTime.now()) extends Damageable:
    private val collider: HitBox = HitBoxRectangular(Point2D(_position.x + Constants.missileBatteryBaseSize/2, _position.y + Constants.missileBatteryHeightSize/2),
                                                    Constants.missileBatteryBaseSize,
                                                    Constants.missileBatteryHeightSize,
                                                    Angle.Degree(0)) //collider of the object

    /**
     * @return the position of the object
     */
    def getPosition: Point2D = _position

    /**
     * @return true: If the turret is reloading and still not able to shoot
     */
    def isReloading: Boolean = time.LocalDateTime.now().isBefore(_lastShoot.plusSeconds(Constants.reloadingTime))

    /**
     * @return a tuple of MissileBattery and Missile if turret is ready for shoot, None if turret is reloading
     */
    def shootRocket(endingPoint: Point2D): Option[Tuple2[MissileBattery, MissileBattery]] = //TODO finire implementare con missile
        if !this.isReloading then
            Some((MissileBattery(getPosition), MissileBattery(getPosition))) //If not reloading, allow shoot
        else
            None

    /**
     * @return a string containing all the valuable informations
     */
    override def toString: String = "Missile battery --> Position: x:" + getPosition.x + " y:" + getPosition.y + "; Reloading: " + isReloading + ";"

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
    override def currentLife: LifePoint = _life

    /**
     * @param damage the damage that the object received.
     * @return the object with the new health.
     */
    override def takeDamage(damage: LifePoint): Damageable = MissileBattery(_position, _life - damage, _lastShoot)