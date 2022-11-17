package model.spawner

import model.DeltaTime
import model.missile.Missile
import model.spawner.GenericSpawner
import org.scalatest.events.TestFailed
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

object GenericSpawnerTest:
  val interval: DeltaTime = 1
  val maxWidth = 100
  val maxHeight = 200
  val timePassed: DeltaTime = 5
  val largeTimePassed: DeltaTime = 50

class GenericSpawnerTest extends AnyFunSpec :
  import GenericSpawnerTest._

  given Random()

  describe("A spawner") {
    describe("of missiles") {
      describe("after a specific period of time") {
        it("should generate n missiles") {
          val spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
          val newSpawner = spawner.timeElapsed(timePassed)
          val (missiles, _) = newSpawner.spawn()
          missiles(0) match
            case m: Missile => assertResult(timePassed)(missiles.size)
            case _ => throw IllegalStateException()
        }
      }
      describe("after a specific amount of time that exceeds a fixed threshold") {
        it("should decrease the interval time between single spawns") {
          val spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
          val newSpawner = spawner.timeElapsed(largeTimePassed)
          assert(newSpawner.spawn()._1.size > largeTimePassed)
        }
        it("should decrease the interval in a way based on the sigmoidal function") {
          val spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
          val newSpawner = spawner.timeElapsed(largeTimePassed)
          val newInterval: DeltaTime = interval - (interval * 1 / ( 1 + Math.exp((-interval / 4) * largeTimePassed)) - interval / 2)
          val step: Int = (largeTimePassed / newInterval).toInt
          val size = newSpawner.spawn()._1.size
          assert(size equals step)
        }
      }
    }
  }


