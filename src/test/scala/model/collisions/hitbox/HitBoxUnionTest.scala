package model.collisions.hitbox

import model.collisions.hitbox.*
import model.collisions.Distance
import model.elements2d.Point2D
import model.elements2d.Point2D.GivenEquality.given
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funspec.AnyFunSpec

import scala.math.BigDecimal.double2bigDecimal

object HitBoxUnionTest:
  private val tolerance: Double = 0.1

  private given distance: Distance = 1.0

  private given equality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(tolerance)

class HitBoxUnionTest extends AnyFunSpec :

  import HitBoxUnionTest.{*, given}

  describe("An hit box") {

    describe("that is the union of other hit boxes") {

      it("should be empty if there are no hit boxes") {
        val hitBox = HitBoxUnion()
        assert(hitBox == HitBoxEmpty)
      }

      it("should be the same hit box if there is only one hit box") {
        val hitBox = HitBoxUnion(HitBoxCircular(Point2D(0, 0), 1))
        assert(hitBox == HitBoxCircular(Point2D(0, 0), 1))
      }

      it("should be able to be create from multiple hit box check not null") {
        val hitBox = HitBoxUnion(
          HitBoxPoint(Point2D(0, 0)),
          HitBoxPoint(Point2D(1, 1))
        )
        assert(hitBox != null)
      }

      it("should be able to be create from multiple hit box check content") {
        val point1 = Point2D(0, 0)
        val point2 = Point2D(1, 1)
        val hitBox = HitBoxUnion(
          HitBoxPoint(point1),
          HitBoxPoint(point2)
        )
        assert(hitBox.contains(point1))
        assert(hitBox.contains(point2))
        assert(!hitBox.contains(Point2D(2, 2)))
      }

      it("should be the first hit box if the other is empty (check inside)") {
        val point = Point2D(0, 0)
        val circularHitBox = HitBoxCircular(point, 1)
        val hitBox = HitBoxUnion(
          circularHitBox,
          HitBoxEmpty
        )
        assert(circularHitBox.area.forall(hitBox.contains))
      }

      it("should be the first hit box if the other is empty (check interval)") {
        val point = Point2D(0, 0)
        val circularHitBox = HitBoxCircular(point, 1)
        val hitBox = HitBoxUnion(
          HitBoxEmpty,
          circularHitBox
        )
        assert(hitBox.xMax == circularHitBox.xMax)
        assert(hitBox.xMin == circularHitBox.xMin)
        assert(hitBox.yMax == circularHitBox.yMax)
        assert(hitBox.yMin == circularHitBox.yMin)
      }

      it("should be the first hit box if the other is empty (check outside)") {
        val point = Point2D(0, 0)
        val circularHitBox = HitBoxCircular(point, 1)
        val hitBox = HitBoxUnion(
          circularHitBox,
          HitBoxEmpty
        )
        for
          x <- -1 to 1 by 0.1
          y <- -1 to 1 by 0.1
          point = Point2D(x.toDouble, y.toDouble)
          if !circularHitBox.contains(point)
        yield
          assert(!hitBox.contains(point))
      }

      it("should be the union of the given hit boxes (check interval)") {
        val point1 = Point2D(0, 0)
        val point2 = Point2D(-2, 2)
        val pointHitBox = HitBoxPoint(point2)
        val circularHitBox = HitBoxCircular(point1, 1)
        val hitBox = HitBoxUnion(
          pointHitBox,
          circularHitBox
        )
        assert(hitBox.xMax == circularHitBox.xMax)
        assert(hitBox.xMin == pointHitBox.xMin)
        assert(hitBox.yMax == pointHitBox.yMax)
        assert(hitBox.yMin == circularHitBox.yMin)
      }

      it("should be the union of the given hit boxes (check inside)") {
        val point1 = Point2D(0, 0)
        val point2 = Point2D(-2, 2)
        val pointHitBox = HitBoxPoint(point2)
        val circularHitBox = HitBoxCircular(point1, 1)
        val hitBox = HitBoxUnion(
          pointHitBox,
          circularHitBox
        )
        assert(circularHitBox.area.forall(hitBox.contains))
        assert(hitBox.contains(point2))
      }

      it("should be the union of the given hit boxes (check outside)") {
        val point1 = Point2D(0, 0)
        val point2 = Point2D(-2, 2)
        val pointHitBox = HitBoxPoint(point2)
        val circularHitBox = HitBoxCircular(point1, 1)
        val hitBox = HitBoxUnion(
          pointHitBox,
          circularHitBox
        )
        for
          x <- -2 to 1 by 0.1
          y <- -1 to 2 by 0.1
          point = Point2D(x.toDouble, y.toDouble)
          if !circularHitBox.contains(point) && (point !== point2)
        yield
          assert(!hitBox.contains(point))
      }

      it("should have the points in the hit box returned only once") {
        val point1 = Point2D(0, 0)
        val point2 = Point2D(-2, 2)
        val hitBox = HitBoxUnion(
          HitBoxPoint(point1),
          HitBoxPoint(point2),
          HitBoxPoint(point1)
        )
        val iterator = hitBox.area
        assert(iterator.next() === point1)
        assert(iterator.next() === point2)
        assert(!iterator.hasNext)
      }
    }
  }