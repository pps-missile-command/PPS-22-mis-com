package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.Game
import monix.eval.Task

/**
 * Object that return an update function for the world to be update with the aftermath of its components collisions
 */
object CollisionsDetection:

  /**
   * Apply function used to update the world to be update with the aftermath of its components collisions
   *
   * @return An Update that update the world to be update with the aftermath of its components collisions
   */
  def apply(): Update = on[TimePassed] { (_: Event, game: Game) =>
    Task {
      game
        .checkCollisions
        .updateScore()
    }
  }