package model

import model.World.{height, width}
import model.spawner.GenericSpawner
import model.missile.Missile
import scala.util.Random

/**
 * Class representing the game
 *
 * @param player  the player of the game
 * @param spawner the spawner in the game
 * @param world   the world of the game
 */
case class Game(player: Player, spawner: GenericSpawner[Missile], world: World)

/**
 * Companion object of the game
 */
object Game:
  /**
   * The initial state of the game
   *
   * @return the initial state of the game
   */
  def initialGame: Game =
    Game(Player.initialPlayer, model.spawner.standardSpawner(using Random()), World.initialWorld)
