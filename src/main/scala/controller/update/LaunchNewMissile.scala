package controller.update

import controller.Event
import controller.Event.LaunchMissileTo
import controller.update.Update.on
import model.{World, Game}
import model.behavior.Moveable
import model.collisions.Collisionable
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
      val (ground, missile) = game.world.ground.shootMissile(event.position)
      val newCollisionable =
        missile match
          case Some(missile) => game.world.collisionables + missile
          case None => game.world.collisionables
      game.copy(world = game.world.copy(collisionables = newCollisionable, ground = ground))
    }
  }
