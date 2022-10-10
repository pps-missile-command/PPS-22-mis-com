package model.ground

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint}
import model.elements2d._
import utilities.Constants

case class City(val position: Point2D, val life: LifePoint = Constants.cityInitialLife) extends Damageable:
    private val collider: HitBox = HitBoxRectangular(Point2D(position.x + Constants.cityBaseSize/2, position.y + Constants.cityHeightSize/2),
                                                    Constants.cityBaseSize,
                                                    Constants.cityHeightSize,
                                                    Angle.Degree(0))

    /**
     * @return string containing all the informations about the city
     */
    override def toString: String = "City --> Position: x:" + position.x + " y:" + position.y + "; Life: " + life + "\n"

    /**
     *  @return the initial health of the object.
     */
    override def initialLife: LifePoint = Constants.cityInitialLife

    /**
     *  @return the current health of the object.
     */
    override def currentLife: LifePoint = life

    /**
     * @param damage the damage that the object received.
     *  @return the object with the new health.
     */
    override def takeDamage(damage: Int): City = City(position, life - damage)

    /**
     *  @return the affiliation of the object.
     */
    override def affiliation: Affiliation = Affiliation.Friendly

    /**
     *  @return the hit box of the object.
     */
    override def hitBox: HitBox = collider