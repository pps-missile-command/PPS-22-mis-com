package model.spawner

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.Collisionable

import scala.util.Random

trait SpawnerAggregator[A <: Collisionable] extends GenericSpawner[A]:

  override def spawn(): (Set[A], SpawnerAggregator[A])

  override def timeElapsed(dt: DeltaTime): SpawnerAggregator[A]

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


