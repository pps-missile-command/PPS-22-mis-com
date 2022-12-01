package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.Game
import monix.eval.Task

/**
 * Object that return an update function for the world to update the position its components
 */
object UpdatePosition:

  /**
   * Apply function used to update the position of the game components
   *
   * @return An Update that update the position of the game components
   */
  def apply(): Update = on[TimePassed] { (_: Event, game: Game) =>
    Task {
      game.moveElements()
    }
  }