package model

import model.Timer.Timer
import model.collisions.Collisionable
import model.ground.Ground
import model.missile.Missile
import model.spawner.{GenericSpawner, Spawner, SpecificSpawners}
import view.ViewConstants

import scala.util.Random

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
  given Random()
  def initialWorld: World = World(Ground(), model.spawner.standardSpawner, Set.empty[Collisionable], 0, Timer(0))

/**
 * Class that represents the world.
 *
 * @param ground         the ground of the player in the world.
 * @param spawner        the spawner of the enemies in the world.
 * @param collisionables the collisionables in the world.
 * @param score          the current score of the player.
 */
case class World(ground: Ground, spawner: GenericSpawner[Missile], collisionables: Set[Collisionable], score: ScorePoint, timer: Timer)
