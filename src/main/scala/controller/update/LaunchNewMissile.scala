package controller.update

import controller.Event
import controller.Event.LaunchMissileTo
import controller.update.Update.on
import model.World
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
  def apply(): Update = on[LaunchMissileTo] { (event: LaunchMissileTo, world: World) =>
    Task {
      val (ground, missile) = world.ground.shootMissile(event.position)
      missile match
        case Some(missile) => world.copy(collisionables = world.collisionables :+ missile, ground = ground)
        case None => world.copy(collisionables = world.collisionables, ground = ground)
    }
  }
