package model

import model.World.{height, width}
import view.ViewConstants

import scala.util.Random

package object spawner:
  
  val threshold: Int = 50

  /**
   * Aggregated spawner composed by a list of [[GenericSpawner]]
   * @param Random The Random instance
   * @return the new  [[SpawnerAggregator]]
   */
  def standardSpawner(using Random) = SpawnerAggregatorImpl(
    List(
      GenericSpawner(3, spawnable = SpecificSpawners.MissileStrategy(width, height)),
      GenericSpawner(10, spawnable = SpecificSpawners.ZigZagStrategy(width, height))
    ):_*
  )

  /**
   * Extension method that permits to pop n elements from a generic LazyList
   */
  extension[A](list: LazyList[A])
    def popN(n: Int): (Set[A], LazyList[A]) = (list.take(n).toSet, list.drop(n))

  /**
   * Extension methods for DeltaTime
   */
  extension(interval: DeltaTime)

    /**
     * The hyperbole function that given a timePassed retun the updated missile
     */
    def ~(timePassed: DeltaTime) : DeltaTime = interval - interval * (1 / ( 1 + Math.exp((-interval / 4) * timePassed)) - interval / 2)
    /**
     * Function that given a condition and a world può inficiare positivamente sulle lezaione.
     * @param condition The condition to which we can subscreibe
     * @param mapper
     * @return
     */
    def mapIf(condition: () => Boolean)(mapper: (DeltaTime) => DeltaTime): DeltaTime = condition match
      case _ if condition() => mapper(interval)
      case _ => interval