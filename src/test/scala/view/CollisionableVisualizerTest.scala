package view

import model.spawner.{GenericSpawner, Spawner, SpecificSpawners}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class CollisionableVisualizerTest extends AnyFunSpec :

  given Conversion[Double, Int] with
    override def apply(x: Double): Int = x.toInt

  describe("A collisionable visualizer") {
    describe("that convert collisionable elements into graphic ones") {
      it("should return a view element of all missiles") {
        given Random()
        val spawner = GenericSpawner(1, spawnable = SpecificSpawners.MissileStrategy(10,10))
        val newSpawner = spawner.timeElapsed(10)
        val missiles = newSpawner.spawn()._1
        val visualElements = CollisionableVisualizer.printElements(missiles.toSet)
        assert(visualElements.size == 10)
      }
    }
  }