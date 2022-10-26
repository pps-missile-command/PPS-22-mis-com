package model.ground

import model.collisions.Affiliation
import model.elements2d.Point2D
import model.{DeltaTime, ground}
import model.ground.MissileBattery
import model.missile.Missile
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec
import utilities.Constants

import java.time

class MissileBatteryTest extends AnyFunSpec with BeforeAndAfterAll{

    val xTest = 3.0
    val yTest = 4.0
    val point = Point2D(xTest, yTest)
    var testdt: DeltaTime = 0;

    override def beforeAll(): Unit = {
        super.beforeAll()
        testdt = 0;
    }

    describe("A missile battery") {
        it("should create a new turret in given position and with specified life") {
            val batteryTurret = MissileBattery(point)
            assert(batteryTurret.bottomLeft_Position.x === xTest)
            assert(batteryTurret.bottomLeft_Position.y === yTest)
            assert(batteryTurret.initialLife === Constants.missileBatteryInitialLife)
            assert(batteryTurret.currentLife === Constants.missileBatteryInitialLife)
        }

        it("should fail if you shoot twice in a row without waiting") {
            val batteryTurret = ground.MissileBattery(point)
            assert(!batteryTurret.isReadyForShoot(testdt)) //false. When turret got created, the reload will start
            testdt = testdt + 2000
            assert(!batteryTurret.isReadyForShoot(testdt)) //false. Turret is still reloading
            testdt = testdt + 1000
            assert(batteryTurret.isReadyForShoot(testdt)) //true. Turret finished reloading
        }
        it("should be a friendly unit") {
            val batteryTurret = ground.MissileBattery(point)
            assert(batteryTurret.affiliation === Affiliation.Friendly)
        }

        it("should fail if you shoot right after creating a turret") {
            val batteryTurret = ground.MissileBattery(point)
            assert(batteryTurret.shootRocket(point, 0).isEmpty)
        }
        it("should pass if you shoot after waiting for the reload time") {
            val batteryTurret = ground.MissileBattery(point)
            testdt = 3000
            val values = batteryTurret.shootRocket(Point2D(10.0, 10.0), testdt)
            assert(values.nonEmpty)
            
            val tupled: Tuple2[MissileBattery, Missile] = values.get
            assert(!tupled._1.isReadyForShoot(testdt)) //true. The turret shot, so is reloading
            assert(tupled._2.affiliation === Affiliation.Friendly)
            assert(tupled._2.destination === Point2D(10.0, 10.0))
        }

        it("should get damaged and destroied if it have 0HP") {
            val batteryTurret = ground.MissileBattery(point)
            val batteryTurret2 = batteryTurret.takeDamage(3)
            assert(batteryTurret2.currentLife === 0)
            assert(batteryTurret2.isDestroyed)
        }
    }
}