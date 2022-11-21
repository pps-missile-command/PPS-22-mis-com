package controller.update

import controller.Event
import monix.eval.Task
import controller.Event.*
import model.*
import controller.update.Update.*
import model.behavior.Timeable
import model.collisions.Collisionable
import model.ground.Ground


/**
 * Object that return an update function for the world to update the time of its components
 */
object UpdateTime:

  extension (collisionable: Collisionable)
    private def updateTimebleTime(elapsedTime: DeltaTime): Collisionable = collisionable match
      case timeable: Timeable => timeable.timeElapsed(elapsedTime).asInstanceOf[Collisionable]
      case _ => collisionable

  /**
   * Apply function used to update the time of the game components
   *
   * @return An Update that update the time of the game components
   */
  def apply(): Update = on[TimePassed] { (event: TimePassed, game: Game) =>
    Task {
      val collisionables = game.world.collisionables.map(_.updateTimebleTime(event.deltaTime))
      val spawner = game.spawner.timeElapsed(event.deltaTime)
      val ground = Ground(game.world.ground.cities, game.world.ground.turrets.map(_.timeElapsed(event.deltaTime)))
      val player = game.player.copy(timer = game.player.timer.timeElapsed(event.deltaTime))
      Game(player, spawner, World(ground, collisionables))
    }
  }
