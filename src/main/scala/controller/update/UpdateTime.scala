package controller.update

import controller.Event
import monix.eval.Task
import controller.Event._
import model.Game
import controller.update.Update._


/**
 * Object that return an update function for the world to update the time of its components
 */
object UpdateTime:

  /**
   * Apply function used to update the time of the game components
   *
   * @return An Update that update the time of the game components
   */
  def apply(): Update = on[TimePassed] { (event: TimePassed, game: Game) =>
    Task {
      game.timeElapsed(event.deltaTime)
    }
  }
