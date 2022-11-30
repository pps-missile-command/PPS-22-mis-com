package model.missile.zigzag

import model.elements2d.{Angle, Point2D, Vector2D}
import org.scalatest.funspec.AnyFunSpec
import model.missile.zigzag.PimpingByVector2D.-|-

import scala.util.Random

object DirectionListTest:
  extension(p: Point2D)
    def round() = Point2D(
      BigDecimal(p.x).setScale(4, BigDecimal.RoundingMode.HALF_UP).toDouble,
      BigDecimal(p.y).setScale(4, BigDecimal.RoundingMode.HALF_UP).toDouble)

class DirectionListTest extends AnyFunSpec :
  import DirectionListTest._

  given Random()

  describe("A 2D point") {
    describe("over a vector 2D") {
      describe("rotated by 90 degrees on the right") {
        it("should move on the right") {
          val p = Point2D(5,5)
          val v = Vector2D(1,1)
          val v1 = v -|- Right
          val newP = p --> (-v1)
          val v2 = Vector2D(defaultMagnitude, Angle.Degree(v.direction.get.degree + 90))
          val newP1 = p --> (-v2)
          assert(newP.round() == newP1.round())
        }
      }
      describe("rotated by 90 degrees on the left") {
        it("should move on the left") {
          val p = Point2D(5,5)
          val v = Vector2D(1,1)
          val v1 = v -|- Left
          val newP = p --> (-v1)
          val v2 = Vector2D(defaultMagnitude, Angle.Degree(v.direction.get.degree - 90))
          val newP1 = p --> (-v2)
          assert(newP.round() == newP1.round())
        }
      }
    }
  }
  describe("A random list of points") {
    describe("between two points") {
      describe("and a given step of n") {
        it("should have n elements") {
          val step = 5
          val list = DirectionList.randomList(Point2D(0, 0), Point2D(5,5), step).take(step)
          assert(list.size == step)
        }
      }
    }
  }
  describe("A zigzag list of points") {
    describe("between two points") {
      describe("and a given step of n") {
        it("should have n-1 elements (without the starting point)") {
          val step = 5
          val list = DirectionList(Point2D(0, 0), Point2D(5,5), step, maxWidth = 20)
          assert(list.size == (step - 1))
        }
      }
    }
  }