package model.spawner
import model.collisions.Affiliation
import model.elements2d.Point2D
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object SpawnerTest:
  val maxHeight = 500
  val maxWidth = 300

class SpawnerTest extends AnyFunSpec :
   import SpawnerTest._

   describe("A spawner of enemy missiles") {
     describe("with an interval of a missile per second") {
       it("should generate enemy missiles") {
         val spawner = Spawner(1, maxWidth, maxHeight)
         val missiles = spawner.spawn(1)
         if(!missiles.isEmpty)
          assert(missiles(0).affiliation == Affiliation.Enemy)
       }
       it("should generate empty list if no enough time is passed") {
         val spawner = Spawner(1, maxWidth, maxHeight)
         val missiles = spawner.spawn(0.5)
         assert(missiles.size == 0)
       }
       it("should generate a missile after 1 second of virtual time") {
         val spawner = Spawner(1, maxWidth, maxHeight)
         val missiles = spawner.spawn(1)
         assert(missiles.size == 1)
         assert(missiles(0).affiliation == Affiliation.Enemy)
       }
       it("should generate n missiles after n seconds of virtual time") {
         val timePassed = 10
         val spawner = Spawner(1, maxWidth, maxHeight)
         val missiles = spawner.spawn(timePassed)
         assert(missiles.size == timePassed)
       }
     }
     describe("with an interval of a missile every 0.33 second") {
       it("should generate  missiles after 1 second of virtual time") {
         val timePassed = 1
         val spawner = Spawner(0.33, maxWidth, maxHeight)
         val missiles = spawner.spawn(timePassed)
         assert(missiles.size == 3)
       }
     }
   }

