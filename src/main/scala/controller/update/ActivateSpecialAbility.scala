package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.Game
import monix.eval.Task

/**
 * Object that return an update function for the world to be update with the special abilities of its components
 */
object ActivateSpecialAbility:

  /**
   * Apply function used to update the world to be update with the special abilities of its components
   *
   * @return An Update that update the world to be update with the special abilities of its components
   */
  def apply(): Update = on[TimePassed] { (_: Event, game: Game) =>
    Task {
      game.activateSpecialAbility
    }
  }
