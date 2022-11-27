package model

import model.World.{height, width}
import model.collisions.Collisionable
import view.ViewConstants
import model.spawner.SpawnerAggregator.SpawnerAggregatorImpl

import scala.util.Random

package object spawner:
  
  val threshold: Int = 20

  /**
   * Aggregated spawner composed by a list of [[GenericSpawner]]
   * @param Random The Random instance
   * @return the new  [[SpawnerAggregator]]
   */
  def standardSpawner(using Random): GenericSpawner[Collisionable] = SpawnerAggregatorImpl(
    List(
      GenericSpawner(3, SpecificSpawners.MissileStrategy(width, height)),
      GenericSpawner(6, SpecificSpawners.ZigZagStrategy(width, height)),
      GenericSpawner(3, spawnable = SpecificSpawners.PlaneStrategy(height))
    ):_*
  )

  /**
   * Extension methods for DeltaTime
   */
  extension(interval: DeltaTime)

    /**
     * The hyperbole function that given a timePassed return the updated missile
     */
    def ~(timePassed: DeltaTime) : DeltaTime =
      interval - ((interval * (1 / ( 1 + Math.exp(((-1 * timePassed) / 10))))) - (interval / 2))
    /**
     * Function that given a condition and a world può inficiare positivamente sulle lezaione.
     * @param condition The condition to which we can subscreibe
     * @param mapper
     * @return
     */
    def mapIf(condition: () => Boolean)(mapper: (DeltaTime) => DeltaTime): DeltaTime = condition match
      case _ if condition() => mapper(interval)
      case _ => interval