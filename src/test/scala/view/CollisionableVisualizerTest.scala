package view

import model.spawner.Spawner
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object CollisionableVisualizerTest:
  ???

class CollisionableVisualizerTest extends AnyFunSpec :
  import CollisionableVisualizerTest._

  describe("A collisionable visualizer") {
    describe("that convert collisionable elements into graphic ones") {
      it("should return a view element of all missiles") {
        val spawner = Spawner(1, 10, 10)
        val newSpawner = spawner.timeElapsed(10)
        val missiles = spawner.spawn(10)
        val visualElements = CollisionableVisualizer.printElements(missiles)
        assert(visualElements.size == 10)
      }
      it("should return a view element for an explosion") {

      }
    }
  }