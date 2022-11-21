package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.Game
import model.behavior.Moveable
import model.collisions.Collisionable
import monix.eval.Task

/**
 * Object that return an update function for the world to update the position its components
 */
object UpdatePosition:

  extension (collisionable: Collisionable)
    private def updateMovablePosition(): Collisionable =
      collisionable match
        case moveable: Moveable => moveable.move().asInstanceOf[Collisionable]
        case _ => collisionable

  /**
   * Apply function used to update the position of the game components
   *
   * @return An Update that update the position of the game components
   */
  def apply(): Update = on[TimePassed] { (_: Event, game: Game) =>
    Task {
      val collisionables = game.world.collisionables.map(_.updateMovablePosition())
      game.copy(world = game.world.copy(collisionables = collisionables))
    }
  }