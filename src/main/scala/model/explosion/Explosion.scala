package model.explosion

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.hitbox.HitBoxCircular
import model.collisions.{Affiliation, Collisionable, Damager, HitBox, LifePoint}
import model.elements2d.Point2D

/**
 * Trait that defines the model of an Explosion, that is damager because it
 * causes damage to other entities and timeable, to manage the time passing
 */
trait Explosion extends Damager, Timeable:

  /**
   * Function that return the position of the explosion
   * @return the position of the explosion
   */
  def position: Point2D

  /**
   * Function that models the radius of the explosion
   * @return the radius of the explosion
   */
  def radius: Double

  /**
   * Function that models the termination state of the explosion
   * @return true if the explosion has been expired and false if explosion has not terminated yet
   */
  def terminated: Boolean

  /**
   * Override of the timeElapsed method, that defines the time passing
   * @param dt It is the virtual time passed
   * @return The new explosion with the delta time updated
   */
  override def timeElapsed(dt: DeltaTime): Explosion

/**
 * Companion object for [[Explosion]]
 */
object Explosion:

  /**
   * Factory method used to create a new Explosion
   * @param damageToInflict Damage to inflict against all the damageable that enters within the radius
   * @param hitboxRadius The radius of the explosion and of its hitbox
   * @param position Position of the explosion
   * @param dt Current virtual delta time
   * @param maxTime Time to wait for the explosion to be terminated, until this time does not expire the explosion could still inflict damages
   * @param parentAffiliation The same parent affiliation
   * @return The new explosion
   */
  def apply(damageToInflict: LifePoint, hitboxRadius: Double = standardRadius, position: Point2D, dt: DeltaTime = 0, maxTime: DeltaTime = maxTimeExplosion)(using parentAffiliation: Affiliation): Explosion = new Explosion {

    /**
     * Check of the damageToInflict and hitboxRadius validity
     */
    (damageToInflict, hitboxRadius) match
      case (n, m) if n == 0 || m == 0 => throw new IllegalArgumentException()
      case _ => ()

    /**
     * 
     *  @return the position of the explosion
     */
    override def position: Point2D = position

    /**
     * 
     *  @return the radius of the explosion
     */
    override def radius: Double = hitboxRadius match
      case n if n >= 0 => hitboxRadius
      case n if n < 0 => Math.abs(hitboxRadius)

    /**
     *
     * @param _dt Delta time elapsed
     *  @return The new explosion with the delta time updated
     */
    override def timeElapsed(_dt: DeltaTime): Explosion = apply(damageToInflict, hitboxRadius, position, dt + _dt)

    /**
     * 
     *  @return true if the explosion has been expired and false if explosion has not terminated yet
     */
    override def terminated: Boolean = dt >= maxTime

    /**
     * The damage inflicted by this object.
     *
     * @return the damage inflicted by this object.
     */
    override def damageInflicted: LifePoint = damageToInflict match
      case n if n > 0 => damageToInflict
      case n if n < 0 => Math.abs(damageToInflict)

    /**
     * Return the hit box of the object.
     *
     * @return the hit box of the object.
     */
    override protected def hitBox: HitBox = HitBoxCircular(position, hitboxRadius)

    /**
     * Return the affiliation of the object.
     *
     * @return the affiliation of the object.
     */
    override def affiliation: Affiliation = parentAffiliation
  }



