package model.spawner

import model.missile.Missile
import model.missile.zigzag.ZigZagMissile
import model.vehicle.Plane
import org.scalatest.funspec.AnyFunSpec

object SpecificSpawnersTest:
  val genericSpawner = GenericSpawner(2,0,0,_)
  val width: Double = 100
  val height: Double = 100

class SpecificSpawnersTest extends AnyFunSpec:
  import SpecificSpawnersTest._

  describe("A specific spawner") {
    describe("of missiles") {
      val spawner = genericSpawner(SpecificSpawners.MissileStrategy(width, height)).timeElapsed(5)
      val elements = spawner.spawn()
      assert(elements._1.size > 0)
      assert(elements._1.head.isInstanceOf[Missile])
    }
    describe("of ZigZag missiles") {
      val spawner = genericSpawner(SpecificSpawners.ZigZagStrategy(width, height)).timeElapsed(5)
      val elements = spawner.spawn()
      assert(elements._1.size > 0)
      assert(elements._1.head.isInstanceOf[ZigZagMissile])
    }
    describe("of planes") {
      val spawner = genericSpawner(SpecificSpawners.PlaneStrategy(width)).timeElapsed(5)
      val elements = spawner.spawn()
      assert(elements._1.size > 0)
      assert(elements._1.head.isInstanceOf[Plane])
    }
  }
