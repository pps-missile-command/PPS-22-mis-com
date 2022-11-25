package model.vehicle

import model.World
import model.elements2d.{Point2D, Vector2D}
import org.scalatest.funspec.AnyFunSpec
import scala.util.Random

class PlaneTest extends AnyFunSpec:
    given Random()
    describe("A vehicle") {
        it("should create a new vehicle in the gived starting position and certain HP") {
            val vehicle = Plane(vehicleTypes.Left_To_Right, 0)
            assert(vehicle.position === Point2D(0, 0))
            assert(vehicle.currentLife === planeInitialLife)
        }
        it("should get have the correct direction") {
            val vehicle = Plane(vehicleTypes.Left_To_Right, 0)
            val vehicle2 = Plane(vehicleTypes.Right_To_Left, 0)
            assert(vehicle.direction.direction.get.degree === 180)
            assert(vehicle2.direction.direction.get.degree === 0)
        }
        it("should move along right axis") {
            var vehicle = Plane(vehicleTypes.Left_To_Right, 0)
            vehicle = vehicle.timeElapsed(1)
            vehicle = vehicle.move()
            assert(vehicle.position === Point2D(5, 0))

            val tp = 2
            var vehicle2 = Plane(vehicleTypes.Left_To_Right, 0)
            vehicle2 = vehicle2.timeElapsed(tp)
            vehicle2 = vehicle2.move()
            assert(vehicle2.position === Point2D(planeVelocity*tp, 0))
        }
        it("should take damage and get destroied correctly if hitted") {
            var vehicle = Plane(vehicleTypes.Left_To_Right, 0)
            assert(vehicle.takeDamage(2).currentLife === planeInitialLife-2)
            assert(vehicle.takeDamage(3).isDestroyed)
        }
        it("should shoot a missile if method called") {
            var vehicle = Plane(vehicleTypes.Left_To_Right, 0)
            vehicle = vehicle.timeElapsed(50)
            val shootVals = vehicle.spawn()
            assert(shootVals._1.toList.size > 0)
            assert(shootVals._2.isInstanceOf[Plane])
        }
        it("should be with the correct direction type") {
            var vehicle = Plane(vehicleTypes.Left_To_Right, 0)
            var vehicle2 = Plane(vehicleTypes.Right_To_Left, 0)
            assert(vehicle.planeDirection === vehicleTypes.Left_To_Right)
            assert(vehicle2.planeDirection === vehicleTypes.Right_To_Left)
        }
    }
