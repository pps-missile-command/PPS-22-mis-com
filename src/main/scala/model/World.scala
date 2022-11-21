package model

import model.collisions.Collisionable
import model.ground.Ground

/**
 * Object with the values of the world and its initial state.
 */
object World:

  /**
   * The width of the world.
   */
  val width = 700
  
  /**
   * The height of the world.
   */
  val height = 300

  /**
   * The initial state of the world.
   *
   * @return the initial state of the world
   */
  def initialWorld: World = World(Ground(), Set.empty[Collisionable])

/**
 * Class that represents the world.
 *
 * @param ground         the ground of the player in the world.
 * @param collisionables the collisionables in the world.
 */
case class World(ground: Ground, collisionables: Set[Collisionable])
