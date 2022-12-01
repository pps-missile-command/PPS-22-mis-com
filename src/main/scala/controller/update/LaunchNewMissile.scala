package controller.update

import controller.Event
import controller.Event.LaunchMissileTo
import controller.update.Update.on
import model.Game
import monix.eval.Task

/**
 * Object that return an update function for the world to be updated when the user launch a missile.
 */
object LaunchNewMissile:

  /**
   * Apply function used to update the world to be updated when the user launch a missile.
   *
   * @return an update function for the world to be updated when the user launch a missile.
   */
  def apply(): Update = on[LaunchMissileTo] { (event: LaunchMissileTo, game: Game) =>
    Task {
      game.shootMissile(event.position)
    }
  }
