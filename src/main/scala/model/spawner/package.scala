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

    GenericSpawner(3, SpecificSpawners.MissileStrategy(width, height)),
    GenericSpawner(16, SpecificSpawners.ZigZagStrategy(width, height)),
    GenericSpawner(10, SpecificSpawners.SatelliteStrategy(width)),
    GenericSpawner(20, SpecificSpawners.PlaneStrategy(height))
  )