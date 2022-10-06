package model.collisions

/**
 * Trait that represents an object that can be damaged by a collision.
 */
trait Damageable extends Collisionable :

  /**
   * The current health of the object.
   *
   * @return the current health of the object.
   */
  def currentLife: Double

  /**
   * The initial health of the object.
   *
   * @return the initial health of the object.
   */
  def initialLife: Double

  /**
   * The object takes damage.
   *
   * @param damage the damage that the object received.
   * @return the object with the new health.
   */
  def takeDamage(damage: Double): Damageable

  /**
   * returns true if the object is destroyed.
   *
   * @return true if the object is destroyed.
   */
  def isDestroyed: Boolean = currentLife <= 0.0
