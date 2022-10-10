package model.missile

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

  def direction: Vector2D

  override def move(dt: Double): Missile

object Missile:

  /**
   *
   * @param missile
   * @param dt
   * @return
   */
  def BasicMove(missile: Missile)(dt: Double): Missile = missile match
    case m if m.isDestroyed => apply(lifePointDeath, missile.affiliation, missile.damage, missile.velocity, missile.position, missile.finalPosition)
    case _ => apply(missile.currentLife, missile.affiliation, missile.damage, missile.velocity, missile.position --> (missile.direction * missile.velocity * dt), missile.finalPosition)

  def apply(lifePoint: LifePoint, myAffiliation: Affiliation, myDamage: LifePoint, myVelocity: Double, myPosition: Point2D, myFinalPosition: Point2D) : Missile = new Missile with MissileDamageable(lifePoint, myPosition, myFinalPosition):

    override def takeDamage(damage: LifePoint): Damageable = this match
      case m if m.isDestroyed => apply(lifePointDeath, myAffiliation, myDamage, myVelocity, myPosition, myFinalPosition)
      case _ => apply(currentLife - damage, myAffiliation, myDamage, myVelocity, myPosition, myFinalPosition)
        
    override def damage: LifePoint = myDamage

    override def position: Point2D = myPosition

    override def velocity: Double = myVelocity

    override def finalPosition: Point2D = myFinalPosition

    override def direction: Vector2D = (myPosition, myFinalPosition)

    override def affiliation: Affiliation = myAffiliation

    val moveStrategy: MissileMovement = Missile.BasicMove(this)(_)

    override def move(dt: Double): Missile = moveStrategy(dt)
