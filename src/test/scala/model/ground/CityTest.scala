package model.ground

import model.collisions.Affiliation
import model.elements2d.Point2D
import org.scalatest.funspec.AnyFunSpec

class CityTest extends AnyFunSpec {

    private val xTest = 3.0
    private val yTest = 4.0
    val point = Point2D(xTest, yTest)
    val city = City(point)

    describe("A city") {
        it("should create a new city in given position") {
            assert(city.getPosition.x === xTest)
            assert(city.getPosition.y === yTest)
        }
        it("should be friendly and have 3HP") {
            assert(city.affiliation === Affiliation.Friendly)
            assert(city.currentLife === 3)
        }
        it("should take damage and get destroyed if reach 0HP") {
            assert(city.currentLife === 3)
            val city2 = city.takeDamage(3)
            assert(city2.currentLife === 0)
            assert(city2.isDestroyed)
        }
        it("should keep the same position after taking damage") {
            assert(city.currentLife === 3)
            val city2 = city.takeDamage(1).asInstanceOf[City]
            assert(city2.getPosition === city.getPosition)
        }
    }
}
