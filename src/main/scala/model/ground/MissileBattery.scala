package model.ground

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint}
import model.elements2d.*
import model.missile.{Missile, hitboxHeight}
import utilities.Constants

import java.time
import java.time.LocalDateTime

val missileTurretReloadTimems = 3000

case class MissileBattery(val bottomLeft_Position: Point2D,
                          val life: LifePoint = Constants.missileBatteryInitialLife,
                          dt: DeltaTime = 0) extends Damageable, Timeable:


    private val collider: HitBox = HitBoxRectangular(Point2D(bottomLeft_Position.x + Constants.missileBatteryBaseSize/2, bottomLeft_Position.y + Constants.missileBatteryHeightSize/2),
                                                    Constants.missileBatteryBaseSize,
                                                    Constants.missileBatteryHeightSize,
                                                    Angle.Degree(0)) //collider of the object

    /**
     * @return true: If the turret is reloading and still not able to shoot
     */
    def isReadyForShoot(actualdt: DeltaTime): Boolean = actualdt - dt >= missileTurretReloadTimems

    /**
     * @return a tuple of MissileBattery and Missile if turret is ready for shoot, None if turret is reloading
     */
    def shootRocket(endingPoint: Point2D, actualdt: DeltaTime): Option[Tuple2[MissileBattery, Missile]] =
        if this.isReadyForShoot(actualdt) then
            Some((timeElapsed(actualdt),
                Missile(Constants.missileHealth,
                    Constants.missileFriendlyDamage,
                    Constants.missileFriendlySpeed,
                    Point2D(bottomLeft_Position.x,bottomLeft_Position.y+Constants.missileBatteryHeightSize), endingPoint))) //If not reloading, allow shoot
        else
            None

    /**
     * @return a string containing all the valuable informations
     */
    override def toString: String = "Missile battery --> Position: x:" + bottomLeft_Position.x + " y:" + bottomLeft_Position.y + "; Life: " + life + "; Internal digital time: " + dt + "\n"

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
    override def takeDamage(damage: LifePoint): MissileBattery = this.copy(life = life - damage)

    /**
     * @param actualdt Digital time passed since last update
     * @return A MissileBattery with the new digital time
     */
    override def timeElapsed(actualdt: DeltaTime): MissileBattery = this.copy(dt = dt + actualdt)