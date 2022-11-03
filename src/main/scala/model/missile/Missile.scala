package model.missile

import model.behavior.*
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint, lifePointDeath}
import model.elements2d.{Angle, Point2D, Vector2D}
import model.explosion.{Explosion, MaxTime, standardRadius}
import model.DeltaTime

import scala.util.Random

given maxTime: MaxTime = 8

given Conversion[(Point2D, Point2D), Vector2D] with
  override def apply(x: (Point2D, Point2D)): Vector2D = (x._2 <--> x._1).normalize

trait Missile extends Damageable, Moveable:

  def damage: LifePoint

  def position: Point2D

  def velocity: Double

  def direction: Vector2D = (position, destination)

  override def move(): Missile

  def explode: Explosion

trait Scorable(val points: Int) extends Damageable

case class MissileImpl(
                        lifePoint: LifePoint,
                        override val position: Point2D,
                        override val destination: Point2D,
                        dt: DeltaTime = 0,
                        override val affiliation: Affiliation = Affiliation.Friendly,
                        override val damage: LifePoint = damage,
                        override val velocity: Double = velocity
                      ) extends Missile with MissileDamageable(lifePoint, position, destination):

  val moveStrategy: MissileMovement = Missile.BasicMove(this)(_)
  
  override def takeDamage(damageToInflict: LifePoint): Missile = damageToInflict match
    case n if (currentLife - n) <= 0 => newMissile(life = lifePointDeath)
    case n if (n > 0) => newMissile(life = currentLife - n)
    case _ => this

  override def move(): Missile = this match
    case m if m.isDestroyed => newMissile(life = lifePointDeath)
    case _ => newMissile(pos = moveStrategy(dt), _dt = 0)

  override def timeElapsed(dt: DeltaTime): Missile = newMissile(_dt = dt + this.dt)

  override def destinationReached: Boolean = position == destination

  override def explode: Explosion =
    given expAffiliation: Affiliation = affiliation
    Explosion(damage, hitboxRadius = standardRadius, position)

  private def newMissile(
                          life: LifePoint = lifePoint,
                          pos: Point2D = position,
                          _dt: DeltaTime = dt
                        ) = affiliation match
    case Affiliation.Friendly => this.copy(lifePoint = life, position = pos, dt = _dt)
    case Affiliation.Enemy =>
      val score = this.asInstanceOf[Scorable].points
      new MissileImpl(life, pos, destination, _dt, affiliation, damage, velocity) with Scorable(score)
    case _ => throw IllegalStateException()

object Missile:

  def enemyMissile(lifePoint: LifePoint = initialLife,
                   _damage: LifePoint = damage,
                   _velocity: Double = velocity,
                   position: Point2D,
                   finalPosition: Point2D,
                   score: Int = 1): Missile = new MissileImpl(lifePoint, position, finalPosition, 0, affiliation = Affiliation.Enemy, damage = _damage, velocity = _velocity) with Scorable(1)

  def BasicMove(missile: Missile)(dt: DeltaTime): Point2D =
    val distanceToMove = missile.velocity * dt
    val distanceToFinalPosition = missile.position <-> missile.destination
    if distanceToMove >= distanceToFinalPosition
    then missile.destination
    else missile.position --> (missile.direction * distanceToMove * (-1))

  def apply(lifePoint: LifePoint = initialLife,
            _damage: LifePoint = damage,
            _velocity: Double = velocity,
            position: Point2D,
            finalPosition: Point2D) : Missile = MissileImpl(lifePoint, position, finalPosition, dt = 0, damage = _damage, velocity = _velocity)
