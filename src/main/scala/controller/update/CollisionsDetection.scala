package controller.update

import controller.Event
import controller.Event.TimePassed
import controller.update.Update.on
import model.{Game, World, Player, calculateNewScoreBasedOn}
import model.collisions.{Collisionable, Damageable, applyDamagesBasedOn, calculateCollisions, calculateCollisionsWith}
import model.explosion.Explosion
import model.ground.{City, Ground, MissileBattery}
import model.missile.Missile
import monix.eval.Task

/**
 * Object that return an update function for the world to be update with the aftermath of its components collisions
 */
object CollisionsDetection:

  extension (collisionable: Collisionable)
    private def isDestroyed: Boolean = collisionable match
      case damageable: Damageable => damageable.isDestroyed
      case _ => false

    
    private def explodeMissile: Option[Explosion] = collisionable match
      case missile: Missile if missile.isDestroyed => Option(missile.explode)
      case _ => Option.empty

  extension (collisionables: Set[Collisionable])

    private def explosionsOfDestroyedMissiles: Set[Explosion] =
      collisionables
        .map(explodeMissile)
        .filter(_.isDefined)
        .map(_.get)

    private def getCities: List[City] =
      def isCity(collisionable: Collisionable): Boolean = collisionable match
        case _: City => true
        case _ => false

      def toCity(collisionable: Collisionable): City = collisionable match
        case city: City => city

      collisionables
        .filter(isCity)
        .map(toCity)
        .toList

    private def getMissileBattery: List[MissileBattery] =
      def isMissileBattery(collisionable: Collisionable): Boolean = collisionable match
        case _: MissileBattery => true
        case _ => false

      def toMissileBattery(collisionable: Collisionable): MissileBattery = collisionable match
        case missileBattery: MissileBattery => missileBattery

      collisionables
        .filter(isMissileBattery)
        .map(toMissileBattery)
        .toList

    private def splitGroundFromOther: (Ground, Set[Collisionable]) =
      val (collisions, groundElements) =
        collisionables
          .partition(_ match
            case _: MissileBattery => false
            case _: City => false
            case _ => true
          )
      val (missileBatteries, cities) =
        groundElements
          .partition(_ match
            case _: MissileBattery => true
            case _: City => false
          )
      (Ground(cities.getCities, missileBatteries.getMissileBattery), collisions)

  /**
   * Apply function used to update the world to be update with the aftermath of its components collisions
   *
   * @return An Update that update the world to be update with the aftermath of its components collisions
   */
  def apply(): Update = on[TimePassed] { (_: Event, game: Game) =>
    Task {
      val collisionables = game.world.collisionables ++ game.world.ground.cities ++ game.world.ground.turrets
      val (tmpNewCollisionables, collisionsUpdate) = collisionables applyDamagesBasedOn (collisionables calculateCollisionsWith game.world.collisionables)
      val newScore = game.player.score calculateNewScoreBasedOn collisionsUpdate
      val newExplosion = tmpNewCollisionables.explosionsOfDestroyedMissiles
      val (collisionableAfterSecondCollisions, _) = tmpNewCollisionables applyDamagesBasedOn (tmpNewCollisionables calculateCollisionsWith newExplosion)
      val (newGround, newCollisionables) = collisionableAfterSecondCollisions.splitGroundFromOther
      val newNotDestroyedCollisionables = newCollisionables.filterNot(isDestroyed) ++ newExplosion
      val newWorld = World(newGround, newNotDestroyedCollisionables)
      game.copy(world = newWorld, player = game.player.copy(score = newScore))
    }
  }