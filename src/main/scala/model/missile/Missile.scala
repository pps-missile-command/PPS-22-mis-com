package model.missile

import model.behavior.*
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint, lifePointDeath}
import model.elements2d.{Angle, Point2D, Vector2D}
import model.explosion.{Explosion, standardRadius}
import model.{DeltaTime, Scorable}

import scala.util.Random

/**
 * Trait that models the missile entity, that is damageable (it can take damage) and moveable (it can move as the time passes)
 */
trait Missile extends Damageable, Moveable:

  /**
   * The damage of the explosion this missile is able to create
   * @return the damage of the missile's explosion
   */
  def damage: LifePoint

  /**
   * Function that returns the current missile position
   * @return the missile position
   */
  def position: Point2D

  /**
   * Function that returns the missile velocity
   * @return the missile velocity
   */
  def velocity: Double

  /**
   * This function returns the direction of the missile, that is based on the current position and the destination.
   * This is obtained using the convertion method defined into this package object.
   * @return the vector distance of the missile
   */
  def direction: Vector2D = (position, destination)

  /**
   *
   *  @return the new object moved, with its position updated
   */
  override def move(): Missile

  /**
   *
   *  @return true if the destination has been reached, false otherwise
   */
  override def destinationReached: Boolean

  /**
   * This method models the explosion of the missile
   * @return the explosion of the missile
   */
  def explode: Explosion

  /**
   * This is a method that offer a way to easily recreates the current missile with one, all or partial updated properties
   * @param lifePoint the updated lifepoint for the new missile
   * @param point the updated position of the new missile
   * @param dt the updated delta time of the missile
   * @return the new missile
   */
  def newMissile(lifePoint: LifePoint, point: Point2D, dt: DeltaTime): Missile

  /**
   * This property offer a simple way to manage the move strategy of the missile
   */
  val moveStrategy = BasicMove(this)

/**
 * Case class that models the implementation of a real missile.
 * It is used the [[MissileDamageable]] mixin to override and implements all the Damageable
 * methods
 * @param lifePoint the life points of the missile
 * @param position the position of the missile
 * @param destination the destination of the missile
 * @param dt the current delta time of the missile
 * @param affiliation the affiliation of the missile
 * @param damage the damage that the missile is able to inflict with its explosion
 * @param velocity the velocity of the missile
 */
case class MissileImpl(
                        lifePoint: LifePoint = initialLife,
                        override val position: Point2D,
                        override val destination: Point2D,
                        dt: DeltaTime = 0,
                        override val affiliation: Affiliation = Affiliation.Friendly,
                        override val damage: LifePoint = damage,
                        override val velocity: Double = velocity
                      ) extends Missile with MissileDamageable(lifePoint, position, destination):

  /**
   * If the damage to inflict is greater or equals than the current life points it returns
   * a dead missile, otherwise it returns a new missile with its life points decremented by the damage inflicted
   * @param damageToInflict
   *  @return the object with the new health.
   */
  override def takeDamage(damageToInflict: LifePoint): Missile = damageToInflict match
    case n if (currentLife - n) <= 0 => newMissile(life = lifePointDeath)
    case n if (n > 0) => newMissile(life = currentLife - n)
    case _ => this

  /**
   * If the missile is not destroyed performs a move towards its final destination
   *  @return the new object moved, with its position updated
   */
  override def move(): Missile = this match
    case m if m.isDestroyed => newMissile(life = lifePointDeath)
    case _ => newMissile(pos = moveStrategy(dt), _dt = 0)

  /**
   *
   * @param dt the delta time passed as the game go on
   *  @return the new Timeable object with the current virtual time updated
   */
  override def timeElapsed(dt: DeltaTime): Missile = newMissile(_dt = dt + this.dt)

  /**
   *
   *  @return true if the destination has been reached, false otherwise
   */
  override def destinationReached: Boolean = position == destination

  /**
   * Returns the new explosion with the same affiliation of the missile
   *  @return the explosion of the missile
   */
  override def explode: Explosion =
    Explosion(damage, position = position)(using affiliation)
  
  override def newMissile(
                          life: LifePoint = lifePoint,
                          pos: Point2D = position,
                          _dt: DeltaTime = dt
                        ) = affiliation match
    case Affiliation.Friendly => this.copy(lifePoint = life, position = pos, dt = _dt)
    case Affiliation.Enemy =>
      val score = this.asInstanceOf[Scorable].points
      new MissileImpl(life, pos, destination, _dt, affiliation, damage, velocity) with Scorable(score)
    case _ => throw IllegalStateException()

/**
 * Companion object for the missile
 */
object Missile:
  /**
   * Factory method that creates a new enemy missile
   * @param lifePoint The initial life points of the missile
   * @param _damage The damage of the missile
   * @param _velocity The velocity of the missile
   * @param position The start position of the missile
   * @param finalPosition The final position of the missile
   * @param score The score that the user gains if this enemy missile is destroyed by a friendly missile
   * @return the new enemy missile
   */
  def enemyMissile(lifePoint: LifePoint = initialLife,
                   _damage: LifePoint = damage,
                   _velocity: Double = velocity,
                   position: Point2D,
                   finalPosition: Point2D,
                   score: Int = 1): Missile = new MissileImpl(lifePoint, position, finalPosition, 0, affiliation = Affiliation.Enemy, damage = _damage, velocity = _velocity) with Scorable(1)

  /**
   * Factory method to create a simple friendly missile
   * @param lifePoint Initial life points of the missile
   * @param _damage Damage of the missile
   * @param _velocity Velocity of the missile
   * @param position The start position of the missile
   * @param finalPosition The final destination of the missile
   * @return the new friendly missile
   */
  def apply(lifePoint: LifePoint = initialLife,
            _damage: LifePoint = damage,
            _velocity: Double = velocity,
            position: Point2D,
            finalPosition: Point2D) : Missile = MissileImpl(lifePoint, position, finalPosition, dt = 0, damage = _damage, velocity = _velocity)
