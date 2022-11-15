package model.collisions

import model.elements2d.Point2D
import model.collisions.*
import model.collisions.hitbox.{HitBoxEmpty, HitBoxIntersection}

/**
 * Enum representing the different affiliation of an object in game.
 */
enum Affiliation:
  case Friendly, Enemy, Neutral

/**
 * Trait representing an object that can collide with other objects.
 *
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
   * @param other    the other object.
   * @param distance the distance between two points in the hit box area.
   * @return true if the object is colliding with the given object.
   */
  def isCollidingWith(other: Collisionable)(using distance: Distance): Boolean =
    HitBoxIntersection(hitBox, other.hitBox) != HitBoxEmpty

  /**
   * Return the affiliation of the object.
   *
   * @return the affiliation of the object.
   */
  def affiliation: Affiliation



