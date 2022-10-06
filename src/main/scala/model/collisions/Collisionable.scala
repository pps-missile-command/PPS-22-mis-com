package model.collisions

import model.elements2d.Point2D
import model.collisions.*
import model.collisions.hitbox.HitBoxIntersection

/**
 * Enum representing the different affiliation of an object in game.
 */
enum Affiliation:
  case Friendly, Enemy, Neutral

/**
 * Trait representing an object that can collide with other objects.
 */
trait Collisionable:

  /**
   * Return the hit box of the object.
   *
   * @return the hit box of the object.
   */
  protected def hitBox: HitBox

  /**
   * Return true if the object is colliding with the given object.
   *
   * @param other the other object.
   * @param step  the distance of the point to test the collision.
   * @return true if the object is colliding with the given object.
   */
  def isCollidingWith(other: Collisionable)(using step: Double): Boolean

  /**
   * Return the affiliation of the object.
   *
   * @return the affiliation of the object.
   */
  def affiliation: Affiliation



