package model.explosion

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.hitbox.HitBoxCircular
import model.collisions.{Affiliation, Collisionable, Damager, HitBox, LifePoint}
import model.elements2d.Point2D

trait Explosion extends Damager, Timeable:

  def position: Point2D

  def radius: Double

  def terminated: Boolean

  override def timeElapsed(dt: DeltaTime): Explosion

object Explosion:

  def apply(damageToInflict: LifePoint, hitboxRadius: Double, myPosition: Point2D, dt: DeltaTime = 0)(using maxTime: MaxTime, parentAffiliation: Affiliation): Explosion = new Explosion {

    (damageToInflict, hitboxRadius) match
      case (n, m) if n == 0 || m == 0 => throw new IllegalArgumentException()
      case _ => ()

    override def position: Point2D = myPosition

    override def radius: Double = hitboxRadius match
      case n if n > 0 => hitboxRadius
      case n if n < 0 => Math.abs(hitboxRadius)

    override def timeElapsed(_dt: DeltaTime): Explosion = apply(damageToInflict, hitboxRadius, myPosition, dt + _dt)

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



