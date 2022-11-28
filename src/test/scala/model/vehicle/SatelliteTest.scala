package model.vehicle

import model.World
import model.elements2d.Point2D
import org.scalatest.funspec.AnyFunSpec

import scala.util.Random

class SatelliteTest extends AnyFunSpec {
    given Random()
    describe("A satellite") {
        it("should create a new vehicle in the gived starting position and certain HP") {
            val vehicle = Satellite(50)
            assert(vehicle.position === Point2D(50, World.height))
            assert(vehicle.currentLife === satelliteInitialLife)
        }
        it("should take damage and get destroied correctly if hitted") {
            var vehicle = Satellite(50)
            assert(vehicle.takeDamage(2).currentLife === satelliteInitialLife-2)
            assert(vehicle.takeDamage(3).isDestroyed)
        }
        it("should shoot a missile if method called") {
            var vehicle = Satellite(50)
            vehicle = vehicle.timeElapsed(50)
            val shootVals = vehicle.spawn()
            assert(shootVals._1.toList.size > 0)
            assert(shootVals._2.isInstanceOf[Satellite])
        }
    }
}
