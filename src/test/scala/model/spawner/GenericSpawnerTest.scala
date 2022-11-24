package model.spawner

import model.DeltaTime
import model.collisions.Affiliation
import model.missile.Missile
import model.missile.zigzag.ZigZagMissile
import model.missile.zigzag.ZigZagMissile.*
import model.spawner.GenericSpawner
import org.scalatest.events.TestFailed
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.matchers.should.Matchers.shouldBe

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
          missiles.toList(0) match
            case m: Missile => assertResult(timePassed)(missiles.size)
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
  describe("A spawner of enemy missiles") {
    describe("with an interval of a missile per second") {
      it("should generate enemy missiles") {
        val old_spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
        val spawner = old_spawner.timeElapsed(1)
        val missiles = spawner.spawn()._1
        if(!missiles.isEmpty)
          assert(missiles.toList(0).affiliation == Affiliation.Enemy)
      }
      it("should generate zigzag missiles") {
        val old_spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.ZigZagStrategy(maxWidth, maxHeight))
        val spawner = old_spawner.timeElapsed(1)
        val missiles = spawner.spawn()._1
        if(!missiles.isEmpty)
          missiles.toList(0) shouldBe a[ZigZagMissile]
      }
      it("should generate empty list if no enough time is passed") {
        val old_spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
        val spawner = old_spawner.timeElapsed(0.33)
        val missiles = spawner.spawn()._1
        assert(missiles.size == 0)
      }
      it("should generate a missile after 1 second of virtual time") {
        val old_spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
        val spawner = old_spawner.timeElapsed(1)
        val missiles = spawner.spawn()._1
        assert(missiles.size == 1)
        assert(missiles.toList(0).affiliation == Affiliation.Enemy)
      }
      it("should generate n missiles after n seconds of virtual time") {
        val timePassed = 10
        val old_spawner = GenericSpawner[Missile](interval, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
        val spawner = old_spawner.timeElapsed(timePassed)
        val missiles = spawner.spawn()._1
        assert(missiles.size == timePassed)
      }
    }
    describe("with an interval of a missile every 0.33 second") {
      it("should generate missiles after 1 second of virtual time") {
        val timePassed = 1
        val old_spawner = GenericSpawner[Missile](0.33, spawnable = SpecificSpawners.MissileStrategy(maxWidth, maxHeight))
        val spawner = old_spawner.timeElapsed(timePassed)
        val missiles = spawner.spawn()._1
        assert(missiles.size == 3)
      }
    }
  }


