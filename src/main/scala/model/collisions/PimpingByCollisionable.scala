package model.collisions

import model.behavior.{Moveable, Timeable}
import model.explosion.Explosion
import model.{DeltaTime, Scorable, ScorePoint}
import model.missile.Missile
import model.ground.{City, MissileBattery}

object PimpingByCollisionable:

  extension (collisionable: Collisionable)

    /**
     * Apply the damage to the collisionable, if possible.
     *
     * @param damage the damage to apply
     * @return a new collisionable with the damage applied or the same collisionable if the damage can't be applied
     */
    def applyDamage(damage: LifePoint): Collisionable =
      collisionable match
        case damageable: Damageable if damage > 0 => damageable.takeDamage(damage)
        case _ => collisionable

    /**
     * Get the damage that the collisionable can do to the other collisionable
     *
     * @return the damage that the collisionable can do to the other collisionable
     */
    def damage: LifePoint =
      collisionable match
        case damager: Damager => damager.damageInflicted
        case _ => 0

    /**
     * Check if the collisionable is destroyed.
     *
     * @return true if the collisionable is destroyed, false otherwise
     */

    def isDestroyed: Boolean =
      collisionable match
        case damageable: Damageable => damageable.isDestroyed
        case _ => false

    /**
     * Check if the collisionable is not destroyed.
     *
     * @return false if the collisionable is destroyed, true otherwise
     */
    def isNotDestroyed: Boolean = !isDestroyed


    /**
     * Check if the collisionable is a damager.
     *
     * @return true if the collisionable is a damager, false otherwise
     */
    def isDamager: Boolean =
      collisionable match
        case _: Damager => true
        case _ => false

    /**
     * Check if the collisionable is a scorable.
     *
     * @return true if the collisionable is a scorable, false otherwise
     */
    def isScorable: Boolean =
      collisionable match
        case _: Scorable => true
        case _ => false

    /**
     * Check if the collisionable has affiliation friendly
     *
     * @return true if the collisionable has affiliation friendly, false otherwise
     */
    def isFriendly: Boolean =
      collisionable.affiliation == Affiliation.Friendly

    /**
     * Check if the collisionable hasn't affiliation friendly
     *
     * @return true if the collisionable hasn't affiliation friendly, false otherwise
     */
    def isNotFriendly: Boolean = !isFriendly

    /**
     * Get the score gained with the destruction of the collisionable, if possible.
     *
     * @return the score gained with the destruction of the collisionable, if possible, otherwise 0.
     */
    def getScorePoint: ScorePoint =
      collisionable match
        case scorable: Scorable if scorable.isDestroyed => scorable.points
        case _ => 0

    /**
     * Increase the time of all [[Collisionable]] that are [[Timeable]].
     *
     * @param elapsedTime the elapsed time from last upadte
     * @return a new [[Collisionable]] with the time increased or the same [[Collisionable]] if it is not [[Timeable]]
     */
    def updateTimebleTime(elapsedTime: DeltaTime): Collisionable = collisionable match
      case timeable: Timeable => timeable.timeElapsed(elapsedTime).asInstanceOf[Collisionable]
      case _ => collisionable

    /**
     * Update the position fo the [[Collisionable]] if it is [[Moveable]].
     *
     * @return a new [[Collisionable]] with the position updated or the same [[Collisionable]] if it is not [[Moveable]]
     */
    def updateMovablePosition(): Collisionable =
      collisionable match
        case moveable: Moveable => moveable.move().asInstanceOf[Collisionable]
        case _ => collisionable

    /**
     * Check if the [[Collisionable]] is an [[Explosion]] and if it is terminated.
     *
     * @return true if the [[Collisionable]] is an [[Explosion]] and if it is terminated, false otherwise
     */
    def isExplosionTerminated: Boolean =
      collisionable match
        case explosion: Explosion => explosion.terminated
        case _ => false

    /**
     * Return an [[Option]] with an [[Explosion]] if the [[Collisionable]] is an [[Missile]] and it is destroyed, otherwise return an empty [[Option]].
     *
     * @return
     */
    def explodeMissile: Option[Explosion] =
      collisionable match
        case missile: Missile if missile.isDestroyed => Option(missile.explode)
        case _ => Option.empty

    /**
     * Check if the [[Collisionable]] is an [[City]].
     *
     * @return true if the [[Collisionable]] is an [[City]], false otherwise
     */
    def isCity: Boolean =
      collisionable match
        case _: City => true
        case _ => false

    /**
     * Check if the [[Collisionable]] is an [[MissileBattery]].
     *
     * @return true if the [[Collisionable]] is an [[MissileBattery]], false otherwise
     */
    def isMissileBattery: Boolean =
      collisionable match
        case _: MissileBattery => true
        case _ => false
