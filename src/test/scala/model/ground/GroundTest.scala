package model.ground

import org.scalatest.funspec.AnyFunSpec

class GroundTest extends AnyFunSpec :
    val ground = Ground()
    describe("The ground") {
        it("should be generate the required number of cities and batteries") {
            assert(ground.numberOfCitiesAlive === 6)
            println(ground.turrets)
            assert(ground.numberOfMissileBatteryAlive === 3)
        }
        it("should do something idk what LMAO") {
            ground.missileBatteryAlive.last.takeDamage(2)
            assert(ground.missileBatteryAlive === 2)
        }
    }
