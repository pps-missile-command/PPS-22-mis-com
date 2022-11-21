package model

import model.World.{height, width}
import view.ViewConstants

import scala.util.Random

package object spawner:
  
  val threshold: Int = 50

  def standardSpawner(using Random) = SpawnerAggregatorImpl(
    List(
      GenericSpawner(7, spawnable = SpecificSpawners.MissileStrategy(width, height)),
      GenericSpawner(1, spawnable = SpecificSpawners.ZigZagStrategy(width, height))
    ):_*
  )

  extension[A](list: LazyList[A])
    def popN(n: Int): (Set[A], LazyList[A]) = (list.take(n).toSet, list.drop(n))
  
  extension(interval: DeltaTime)
  
    def ~(timePassed: DeltaTime) : DeltaTime = interval - interval * (1 / ( 1 + Math.exp((-interval / 4) * timePassed)) - interval / 2)
  
    def mapIf(condition: () => Boolean)(mapper: (DeltaTime) => DeltaTime): DeltaTime = condition match
      case _ if condition() => mapper(interval)
      case _ => interval