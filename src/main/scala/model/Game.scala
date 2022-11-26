package model

import model.World.{height, width}
import model.collisions.Collision
import model.spawner.GenericSpawner
import model.missile.Missile
import model.elements2d.Point2D

import scala.util.Random

/**
 * Class representing the game
 *
 */
trait Game extends WorldActions[Game], PlayerActions[Game] :
  /**
   * The player of the game
   */
  val player: Player
  /**
   * The spawner of the game
   */
  val spawner: GenericSpawner[Missile]
  /**
   * The world of the game
   */
  val world: World

  /**
   * Change the actual [[World]] with the given one
   *
   * @param world the new world instance
   * @return a game with the new world
   */
  def updateWorld(world: World): Game

  /**
   * Update the actual [[World]] changing it from the update function
   *
   * @param update function that allow to compute the new world from the actual
   * @return a game with the new world
   */
  def updateWorld(update: World => World): Game = updateWorld(update(world))

  /**
   * Change the actual [[Player]] with the given one
   *
   * @param player the new player instance
   * @return a game with the new player
   */
  def updatePlayer(player: Player): Game

  /**
   * Update the actual [[Player]] changing it from the update function
   *
   * @param update function that allow to compute the new player from the actual
   * @return a game with the new player
   */
  def updatePlayer(update: Player => Player): Game = updatePlayer(update(player))

  /**
   * Change the actual [[GenericSpawner]] with the given one
   *
   * @param spawner the new spawner instance
   * @return a game with the new spawner
   */
  def updateSpawner(spawner: GenericSpawner[Missile]): Game

/**
 * Companion object of the game
 */
object Game:
  /**
   * The initial state of the game, with a initial player and a initial world
   *
   * @return the initial state of the game
   */
  def initialGame: Game =
    Game(Player.initialPlayer, model.spawner.standardSpawner(using Random()), World.initialWorld)

  /**
   * Create a new game with the given player, spawner and world
   *
   * @param player  the player of the game
   * @param spawner the spawner of the game
   * @param world   the world of the game
   * @return a new game with the given player, spawner and world
   */
  def apply(player: Player, spawner: GenericSpawner[Missile], world: World): Game =
    case class GameImpl(player: Player, spawner: GenericSpawner[Missile], world: World) extends Game :
      override def timeElapsed(dt: DeltaTime): Game =
        val newWorld = world.timeElapsed(dt)
        val newSpawner = spawner.timeElapsed(dt)
        val newPlayer = player.updateTimer(player.timer.timeElapsed(dt))
        GameImpl(newPlayer, newSpawner, newWorld)

      override def updateWorld(world: World): Game =
        GameImpl(player, spawner, world)

      override def moveElements: Game =
        updateWorld(world.moveElements)

      override def checkCollisions: (Game, Set[Collision]) =
        val (newWorld, collisions) = world.checkCollisions
        (updateWorld(newWorld), collisions)

      override def activateSpecialAbility: Game =
        val (newMissiles, newSpawner) = spawner.spawn()
        updateWorld(_.activateSpecialAbility)
          .updateSpawner(newSpawner)
          .updateWorld(_.addCollisionables(newMissiles))

      override def shootMissile(destination: Point2D): Game =
        updateWorld(_.shootMissile(destination))

      override def updateScoreByCollisions(collisions: Set[Collision]): Game =
        updatePlayer(player updateScoreByCollisions collisions)

      override def updatePlayer(player: Player): Game =
        GameImpl(player, spawner, world)

      override def updateSpawner(spawner: GenericSpawner[Missile]): Game =
        GameImpl(player, spawner, world)

    GameImpl(player, spawner, world)

  extension (result: (Game, Set[Collision]))

    /**
     * Update score in the game using the collisions in the tuple
     *
     * @return a game with the updated score
     */
    def updateScore(): Game =
      result._1.updateScoreByCollisions(result._2)
