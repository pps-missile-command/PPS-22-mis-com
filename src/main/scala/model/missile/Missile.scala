package model.missile

import model.behavior.{Moveable, Timeable}
import model.collisions.{Affiliation, Collisionable, Damageable, HitBox, LifePoint, lifePointDeath}
import model.elements2d.{Angle, Point2D, Vector2D}

import scala.util.Random

given Conversion[(Point2D, Point2D), Vector2D] with
  override def apply(x: (Point2D, Point2D)): Vector2D = (x._2 <--> x._1).normalize

trait Missile extends Damageable, Moveable:

  def damage: LifePoint

  def position: Point2D

  def velocity: Double

  def finalPosition: Point2D

  def direction: Vector2D = (position, finalPosition)

  override def move(): Missile

trait Scorable(val points: Int) extends Damageable

case class MissileImpl(
                        lifePoint: LifePoint,
                        override val position: Point2D,
                        override val finalPosition: Point2D,
                        dt: DeltaTime,
                        override val affiliation: Affiliation = Affiliation.Friendly,
                        override val damage: LifePoint = damage,
                        override val velocity: Double = velocity
                      ) extends Missile with MissileDamageable(lifePoint, position, finalPosition):

  val moveStrategy: MissileMovement = Missile.BasicMove(this)(_)
  
  override def takeDamage(damage: LifePoint): Missile = this match
    case m if (m.currentLife - m.damage) <= 0 => newMissile(life = lifePointDeath)
    case _ => newMissile(life = currentLife - damage)

  override def move(): Missile = this match
    case m if m.isDestroyed => newMissile(life = lifePointDeath)
    case _ => newMissile(pos = moveStrategy(dt))

  override def timeElapsed(dt: DeltaTime): Timeable = newMissile(_dt = dt)

  private def newMissile(
                          life: LifePoint = lifePoint,
                          pos: Point2D = position,
                          _dt: DeltaTime = dt
                        ) = affiliation match
    case Affiliation.Friendly => this.copy(lifePoint = life, position = pos, dt = _dt)
    case Affiliation.Enemy =>
      val score = this.asInstanceOf[Scorable].points
      new MissileImpl(life, pos, finalPosition, _dt, affiliation, damage, velocity) with Scorable(score)
    case _ => throw IllegalStateException()


object Missile:

  def enemyMissile(lifePoint: LifePoint = initialLife,
                   _damage: LifePoint = damage,
                   _velocity: Double = velocity,
                   position: Point2D,
                   finalPosition: Point2D,
                   score: Int = 1): Missile = new MissileImpl(lifePoint, position, finalPosition, 0, affiliation = Affiliation.Enemy, damage = _damage, velocity = _velocity) with Scorable(1)

  def BasicMove(missile: Missile)(dt: DeltaTime): Point2D = missile.position --> (missile.direction * missile.velocity * dt)

  def apply(lifePoint: LifePoint = initialLife,
            _damage: LifePoint = damage,
            _velocity: Double = velocity,
            position: Point2D,
            finalPosition: Point2D) : Missile = MissileImpl(lifePoint, position, finalPosition, 0, damage = _damage, velocity = _velocity)
