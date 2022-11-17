package model.spawner

import model.DeltaTime
import model.behavior.Timeable
import model.collisions.Collisionable

case class SpawnerAggregator(genericSpawners: GenericSpawner[Collisionable]*):

  def spawn(): Set[Collisionable] = (genericSpawners flatMap { i => i.spawn()._1 }).toSet

  def updateTime(dt: DeltaTime): Unit = genericSpawners map { i => i.timeElapsed(dt) }

