package model.collisions

/**
 * A Trait that represents an that can inflict damage to other objects.
 */
trait Damager extends Collisionable :

  /**
   * The damage inflicted by this object.
   *
   * @return the damage inflicted by this object.
   */
  def damageInflicted: Double