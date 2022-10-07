package model.ground

import model.collisions.Affiliation
import model.elements2d.Point2D
import model.ground
import model.ground.MissileBattery
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funspec.AnyFunSpec

import java.time

class MissileBatteryTest extends AnyFunSpec {

    private val xTest = 3.0
    private val yTest = 4.0
    val point = Point2D(xTest, yTest)

    describe("A missile battery") {
        it("should create a new turret in given position") {
            val batteryTurret = MissileBattery(point)
            assert(batteryTurret.getPosition.x === xTest)
            assert(batteryTurret.getPosition.y === yTest)
        }

        it("should fail if you shoot twice in a row without waiting") {
            val batteryTurret = ground.MissileBattery(point)
            assert(batteryTurret.isReloading) //true. When turret got created, the reload will start
            Thread.sleep(2000)
            assert(batteryTurret.isReloading) //true. Turret is still reloading
            Thread.sleep(1000)
            assert(!batteryTurret.isReloading) //false. Turret finished reloading
        }
        it("should be a friendly unit") {
            val batteryTurret = ground.MissileBattery(point)
            assert(batteryTurret.affiliation === Affiliation.Friendly)
        }

        it("should fail if you shoot right after creating a turret") {
            val batteryTurret = ground.MissileBattery(point)
            assert(batteryTurret.shootRocket.isEmpty)
        }
        it("should pass if you shoot after waiting for the reload time") {
            val batteryTurret = ground.MissileBattery(point)
            Thread.sleep(3000)
            assert(batteryTurret.shootRocket.nonEmpty)
            print(batteryTurret.toString)
        }
    }

}