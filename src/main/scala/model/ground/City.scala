package model.ground

import model.collisions.hitbox.HitBoxRectangular
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint}
import model.elements2d._
import utilities.Constants

case class City(private var _position: Point2D, private var _life: LifePoint = Constants.cityInitialLife) extends Damageable:
    private val collider: HitBox = HitBoxRectangular(Point2D(_position.x + Constants.cityBaseSize/2, _position.y + Constants.cityHeightSize/2),
                                                    Constants.cityBaseSize,
                                                    Constants.cityHeightSize,
                                                    Angle.Degree(0))

    /**
     * @return the position of the object
     */
    def getPosition: Point2D = _position

    /**
     * @return string containing all the informations about the city
     */
    override def toString: String = "City --> Position: x:" + _position.x + " y:" + _position.y + ";"

    /**
     *  @return the initial health of the object.
     */
    override def initialLife: LifePoint = Constants.cityInitialLife

    /**
     *  @return the current health of the object.
     */
    override def currentLife: LifePoint = _life

    /**
     * @param damage the damage that the object received.
     *  @return the object with the new health.
     */
    override def takeDamage(damage: Int): Damageable = City(_position, _life - damage)

    /**
     *  @return the affiliation of the object.
     */
    override def affiliation: Affiliation = Affiliation.Friendly

    /**
     *  @return the hit box of the object.
     */
    override def hitBox: HitBox = collider