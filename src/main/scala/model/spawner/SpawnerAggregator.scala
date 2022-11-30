package model.spawner

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.Collisionable

import scala.util.Random

/**
 * Module that groups the SpawnerAggregator elements
 */
object SpawnerAggregator:
  /**
   * Trait that models a SpawnerAggregator, that is a simple GenericSpawner that encapsulate a set of other
   * GenericSpawners, giving a way to manipulate multiple spawners at once using only one object.
   * @tparam A The type of elements to generate, at least Collisionables for each GenericSpawner
   */
  trait SpawnerAggregator[A <: Collisionable] extends GenericSpawner[A]:
  
    override def spawn(): (Set[A], SpawnerAggregator[A])
  
    override def timeElapsed(dt: DeltaTime): SpawnerAggregator[A]

  /**
   * Implementation of a SpawnerAggregator
   * @param genericSpawners The list of GenericSpawners to wrap into the aggregator
   * @param Random The Random object used for each spawner
   * @tparam R The type of the aggregator, that must be at least Collisionable
   */
  case class SpawnerAggregatorImpl[R <: Collisionable](genericSpawners: GenericSpawner[R]*)(using Random) extends SpawnerAggregator[R]:
    
    override def spawn(): (Set[R], SpawnerAggregator[R]) =
      val list = (genericSpawners map { i => i.spawn() }).toSet
      val v = (list map { i => i._1 } flatMap { i => i }).toSet
      val spawners = (list map { i => i._2 }).toList
      (v, SpawnerAggregatorImpl(spawners:_*))
  
    override def timeElapsed(dt: DeltaTime): SpawnerAggregator[R] =
      SpawnerAggregatorImpl(
        (genericSpawners map { i => i.timeElapsed(dt) }).toList:_*
      )


