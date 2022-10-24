package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.{World, calculateNewScore}
import model.collisions.{Collisionable, Damageable, applyDamage, calculateCollisions}
import model.ground.{City, Ground, MissileBattery}
import model.missile.Missile
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
  def apply(): Update = on[TimePassed] { (_: Event, world: World) =>
    Task {
      def createNewGround(collisionables: List[Collisionable]): (Ground, List[Collisionable]) =
        val (collisions, groundElements) = collisionables.partition(_ match
          case _: MissileBattery => false
          case _: City => false
          case _ => true
        )
        val (missileBatteries, cities) = groundElements.partition(_ match
          case _: MissileBattery => true
          case _: City => false
        )
        (Ground(
          cities.map(_.asInstanceOf[City]),
          missileBatteries.map(_.asInstanceOf[MissileBattery])
        ), collisions)

      def isDestroyed(collisionable: Collisionable): Boolean = collisionable match
        case damageable: Damageable => damageable.isDestroyed
        case _ => false

      val collisionables = world.collisionables ++ world.ground.cities ++ world.ground.turrets
      val collisionableAfterCollisions = applyDamage(calculateCollisions(collisionables))
      val newScore = calculateNewScore(collisionableAfterCollisions, world.score)
      val (newGround, newCollisionables) = createNewGround(collisionableAfterCollisions.keys.toList)
      val newNotDestroyedCollisionables = newCollisionables.filterNot(isDestroyed)
      world.copy(collisionables = newNotDestroyedCollisionables, score = newScore, ground = newGround)
    }
  }