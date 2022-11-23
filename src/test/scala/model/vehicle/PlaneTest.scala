package model.vehicle

import model.World
import model.elements2d.{Point2D, Vector2D}
import org.scalatest.funspec.AnyFunSpec

class PlaneTest extends AnyFunSpec:
    val leftX = 0
    val rightX = 300
    val pointRight = Point2D(rightX, 0)
    val pointLeft = Point2D(leftX, 0)
    describe("A vehicle") {
        it("should create a new vehicle in the gived starting position and certain HP") {
            val vehicle = Plane(pointRight, pointLeft)
            assert(vehicle.position === pointRight)
            assert(vehicle.currentLife === planeInitialLife)
        }
        it("should get have the correct direction") {
            val vehicle = Plane(pointLeft, pointRight)
            assert(vehicle.direction.direction.get.degree === 180)
        }
        it("should move along right axis") {
            var vehicle = Plane(pointLeft, pointRight)
            vehicle = vehicle.timeElapsed(1)
            vehicle = vehicle.move()
            assert(vehicle.position === Point2D(5, 0))

            val tp = 2
            var vehicle2 = Plane(pointRight, pointLeft)
            vehicle2 = vehicle2.timeElapsed(tp)
            vehicle2 = vehicle2.move()
            assert(vehicle2.position === Point2D(rightX - planeVelocity*tp, 0))
        }
        it("should take damage and get destroied correctly if hitted") {
            var vehicle = Plane(pointLeft, pointRight)

            assert(vehicle.takeDamage(2).currentLife === planeInitialLife-2)
            assert(vehicle.takeDamage(3).isDestroyed)
        }
        it("should generate a linear plane and move along line") {
            var vehicle = LinearPlane(vehicleTypes.Left_To_Right, 0)
            assert(vehicle.destination.x === World.width)
            vehicle = vehicle.timeElapsed(999999999).move()
            assert(vehicle.position === Point2D(World.width, 0))
        }
    }
