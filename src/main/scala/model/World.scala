package model

import model.collisions.Collisionable
import model.ground.Ground
import model.spawner.Spawner

/**
 * Object with the values of the world and its initial state.
 */
object World:

  /**
   * The width of the world.
   */
  val width = 400
  
  /**
   * The height of the world.
   */
  val height = 300

  /**
   * The initial state of the world.
   *
   * @return the initial state of the world
   */
  def initialWorld: World = World(Ground(), Spawner(5, width, height), Set.empty[Collisionable], 0)

/**
 * Class that represents the world.
 *
 * @param ground         the ground of the player in the world.
 * @param spawner        the spawner of the enemies in the world.
 * @param collisionables the collisionables in the world.
 * @param score          the current score of the player.
 */
case class World(ground: Ground, spawner: Spawner, collisionables: Set[Collisionable], score: ScorePoint)
