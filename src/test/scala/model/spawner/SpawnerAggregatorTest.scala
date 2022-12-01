package model.spawner

import model.DeltaTime
import model.collisions.Collisionable
import model.missile.Missile
import model.missile.zigzag.ZigZagMissile
import model.missile.zigzag.ZigZagMissile.*
import org.scalatest.funspec.AnyFunSpec
import model.spawner.SpawnerAggregator.SpawnerAggregatorImpl

import scala.util.Random

given Random()

extension[A](l: Set[A])
  def searchOccurrences(f: (v: Int, c: A) => Int) = l.foldLeft(0)(f)

object SpawnerAggregatorTest:
  val interval: DeltaTime = 1
  val maxWidth = 100
  val maxHeight = 200
  val timePassed: DeltaTime = 2
  val missileSpawner = GenericSpawner[Missile](interval, SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
  val zigzagSpawner = GenericSpawner[Missile](interval, SpecificSpawners.ZigZagStrategy(maxWidth, maxHeight))
  val listSpawners = List(missileSpawner, zigzagSpawner)
  val spawnerAggregator = SpawnerAggregatorImpl(listSpawners:_*)

class SpawnerAggregatorTest extends AnyFunSpec :
  import SpawnerAggregatorTest._

  describe("A spawner aggregator") {
    describe("composed by different spawners") {
      it("should spawn elements from each spawner") {
        val newSpawnerAgg = spawnerAggregator.timeElapsed(timePassed)
        val spawnedElements = newSpawnerAgg.spawn()
        assert(spawnedElements._1.size == listSpawners.size * timePassed)

      }
      it("should spawn exactly the number of missiles in case of missile spawners") {
        val newSpawnerAgg = spawnerAggregator.timeElapsed(timePassed)
        val spawnedElements = newSpawnerAgg.spawn()
        val numMissiles = spawnedElements._1.searchOccurrences {
          (i: Int, op: Collisionable) => op match
            case m: Missile => i + 1
            case _ => i
        }
        assert(numMissiles == spawnedElements._1.size)
      }
      it("should spawn exactly the number of missiles in case of zigzag missile spawners") {
        val newSpawnerAgg = spawnerAggregator.timeElapsed(timePassed)
        val spawnedElements = newSpawnerAgg.spawn()
        val numMissiles = spawnedElements._1.searchOccurrences {
          (i: Int, op: Collisionable) => op match
            case m: ZigZagMissile => i + 1
            case _ => i
        }
        assert(numMissiles == spawnedElements._1.filter(_.isInstanceOf[ZigZagMissile]).size)
      }
    }
  }


