package model.collisions

import model.collisions.PimpingByCollisionable._
import model.explosion.Explosion
import model.ground.{City, Ground, MissileBattery}
import model.spawner.GenericSpawner
import model.missile.Missile

/**
 * Object that contains pimping methods for the set of collisionable
 */
object PimpingByCollisionables:

  extension (collisionables: Set[Collisionable])

    /**
     * Activate the special ability of the [[Collisionable]]s.
     * The special ability could be the explosion of a missile or the spawn of a set of new [[Collisionable]].
     *
     * @return the set with all the [[Collisionable]]s
     */
    def activateSpecialAbility: Set[Collisionable] =
      def activateSpecialAbilityToSelf(collisionable: Collisionable): Collisionable =
        collisionable match
          case missile: Missile if missile.destinationReached => missile.explode
          case _ => collisionable

      def activateSpecialAbilityToCollective(collisionable: Collisionable): Set[Collisionable] =
        collisionable match
          case spawner: GenericSpawner[_] with Collisionable =>
            val (spawned, newSpawner) = spawner.spawn()
            spawned.toSet[Collisionable] + newSpawner.asInstanceOf[Collisionable]
          case _ => Set(collisionable)

      collisionables
        .map(activateSpecialAbilityToSelf)
        .flatMap(activateSpecialAbilityToCollective)


    /**
     * Find all the [[Collsionable]]s that can explode make them explode.
     *
     * @return a set with all the [[Explosion]]s
     */
    def explosionsOfDestroyedMissiles: Set[Explosion] =
      collisionables
        .map(explodeMissile)
        .filter(_.isDefined)
        .map(_.get)

    /**
     * Find all the [[Collsionable]]s that are cities.
     *
     * @return a List with all the [[City]]s
     */
    def getCities: List[City] =

      def toCity(collisionable: Collisionable): City = collisionable match
        case city: City => city

      collisionables
        .filter(isCity)
        .map(toCity)
        .toList

    /**
     * Find all the [[Collsionable]]s that are missile batteries.
     *
     * @return a List with all the [[MissileBattery]]s
     */
    def getMissileBattery: List[MissileBattery] =

      def toMissileBattery(collisionable: Collisionable): MissileBattery = collisionable match
        case missileBattery: MissileBattery => missileBattery

      collisionables
        .filter(isMissileBattery)
        .map(toMissileBattery)
        .toList

    /**
     * Find all the [[Collsionable]]s that are part of ground and separate from the other.
     *
     * @return the ground and a set with all the other [[Collsionable]]s that aren't part of ground
     */
    def splitGroundFromOther: (Ground, Set[Collisionable]) =
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

