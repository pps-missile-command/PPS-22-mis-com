package model

import collisions.{Affiliation, HitBox, LifePoint, hitbox}
import model.collisions.hitbox.HitBoxRectangular
import model.elements2d.{Angle, Point2D, Vector2D}
import model.missile.Missile
import model.collisions.Affiliation.*
import model.behavior.*

import scala.util.Random

package object missile:

  val initialLife: LifePoint = 1
  val velocity: Double = 60
  val damage: Int = 1
  val hitboxBase: Double = 5.0
  val hitboxHeight: Double = 10.0
  val maxHeight: Int = 100
  val maxWidth: Int = 50

  /**
   * Given conversion that converts a pair of [[Point2D]] into a [[Vector2D]]
   */
  given Conversion[(Point2D, Point2D), Vector2D] with
    override def apply(x: (Point2D, Point2D)): Vector2D = (x._2 <--> x._1).normalize

  /**
   * This function models the movement of a missile given the virtual delta time passed since its last update.
   * It calculates the distance to move and the distance to the final position, and perform the traslation of the missile's
   * position.
   * @param missile The missile to move
   * @param dt The virtual delta time
   * @return the new missile with its new position updated
   */
  def BasicMove(missile: Missile)(dt: DeltaTime): Point2D =
    val distanceToMove = missile.velocity * dt
    val distanceToFinalPosition = missile.position <-> missile.destination
    if distanceToMove >= distanceToFinalPosition
    then missile.destination
    else missile.position --> (missile.direction * distanceToMove * (-1))
