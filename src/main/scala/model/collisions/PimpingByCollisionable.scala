package model.collisions

import model.{Scorable, ScorePoint}

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
