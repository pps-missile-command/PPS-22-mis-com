package model.ground

import model.DeltaTime
import model.elements2d.Point2D
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec

class GroundTest extends AnyFunSpec with BeforeAndAfterAll :
    var ground = Ground()
    val shootPoint = Point2D(0, 10)
    var testdt: DeltaTime = 0;
    //before each test, it will regenerate the ground as new
    override def beforeAll(): Unit = {
        super.beforeAll()
        ground = Ground()
        testdt = 0;
    }

    describe("The ground") {
        it("should be generate the required number of cities and batteries") {
            assert(ground.numberOfCitiesAlive === 6)
            println(ground.turrets)
            assert(ground.numberOfMissileBatteryAlive === 3)
        }
        it("should remove a turret if damaged") {
            var missileTurret = ground.missileBatteryAlive.map( i => if (i == ground.missileBatteryAlive.last) i.takeDamage(3) else i)
            ground = Ground(ground.cities, missileTurret)
            assert(ground.numberOfMissileBatteryAlive === 2)
        }

        it("should say the player is still alive if more than 1 city is alive") {
            assert(ground.stillAlive)
        }

        it("should create a new ground after shoot with missile and reloading turret") {
            var resultContainer = ground.shootMissile(shootPoint, testdt)
            ground = resultContainer._1
            assert(resultContainer._2.isEmpty) //true. Turrets are reloading, so all of them can't shoot the missile
            testdt = testdt + 3000
            resultContainer = ground.shootMissile(shootPoint, testdt);
            ground = resultContainer._1
            assert(resultContainer._2.nonEmpty) //true. 1 turret shoot the missile
            assert(!ground.turrets(0).isReadyForShoot(0)) //false. 1° turret is reloading because it shooted
            assert(ground.turrets(1).isReadyForShoot(testdt)) //true. 2° and 3° turret didn't shoot
            assert(ground.turrets(2).isReadyForShoot(testdt)) //true. 2° and 3° turret didn't shoot
        }

        it("should handle multiple shoots") {
            ground = ground.shootMissile(shootPoint,3000)._1
            //first missile shooted
            ground = ground.shootMissile(shootPoint, 3000)._1
            //2° missile shooted
            assert(ground._2.nonEmpty) //true. I shooted twice, the missile is shooted from middle battery
            assert(ground.turrets(0).isReadyForShoot(0) == false) //false. Turret is reloading
            assert(ground.turrets(1).isReadyForShoot(0) == false) //false. Turret is reloading
            assert(ground.turrets(2).isReadyForShoot(0)) //true. Turret is ready for shoot
        }

        it("should destroy a city if hitted") {
            val cities = ground.cities.map(c => if ( c == ground.cities.last ) c.takeDamage(3) else c )
            ground = Ground(cities, ground.turrets)
            assert(ground.cities.last.currentLife === 0)
            assert(ground.numberOfCitiesAlive === 5)
            assert(ground.stillAlive)
        }

        it("should destroy all the cities") {
            val cities = ground.cities.map( c => c.takeDamage(3))
            ground = Ground(cities, ground.turrets)
            assert(!ground.stillAlive)
        }

        it("should destroy all the cities and try to shoot") {
            val turrets = ground.turrets.map( t => t.takeDamage(3))
            ground = Ground(ground.cities, turrets)
            var ground2 = ground.shootMissile(shootPoint, 3000)
            assert(ground2._2.isEmpty)
        }

        describe("Test new way of dealing damage") {

            it("should deal damage to a specified city") {
                //use last city as an example
                ground = Ground()
                ground = ground.dealDamage(ground.cities.last, 3)
                assert(ground.numberOfCitiesAlive === 5)
            }
            it("should deal damage to a specified turret") {
                //use last turret as an example
                ground = Ground()
                ground = ground.dealDamage(ground.turrets.last, 3)
                assert(ground.numberOfMissileBatteryAlive === 2)
            }
            it("should deal damage to multiple structures (same type)") {
                ground = Ground()
                val structures = List(ground.turrets(0), ground.turrets(1), ground.turrets(2))
                ground = ground.dealDamage(structures, 3)
                assert(ground.numberOfMissileBatteryAlive === 0)
            }
            it("should deal damage to multiple structures (different types)") {
                ground = Ground()
                val structures = List(ground.turrets(0), ground.turrets(2), ground.cities(5), ground.cities(2))
                ground = ground.dealDamage(structures, 3)
                assert(ground.numberOfMissileBatteryAlive === 1)
                assert(ground.numberOfCitiesAlive === 4)
            }
            it("should deal multiple damage to multiple structures (same type)") {
                ground = Ground()
                val structures = List(ground.turrets(0), ground.turrets(1), ground.turrets(2))
                val damages = List(3, 3, 1)
                ground = ground.dealDamage(structures, damages)
                assert(ground.numberOfMissileBatteryAlive === 1)
            }
            it("should deal multiple damage to multiple structures (different type)") {
                ground = Ground()
                val structures = List(ground.turrets(0), ground.turrets(1), ground.turrets(2),
                                        ground.cities(5), ground.cities(2), ground.cities(4))
                val damages = List(3, 3, 1, 1, 3, 2)
                ground = ground.dealDamage(structures, damages)
                assert(ground.numberOfMissileBatteryAlive === 1)
                assert(ground.numberOfCitiesAlive === 5)
            }
        }
    }
