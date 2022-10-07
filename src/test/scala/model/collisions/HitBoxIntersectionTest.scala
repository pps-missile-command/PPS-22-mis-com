package model.collisions

import model.collisions.hitbox._
import model.elements2d.{Angle, Point2D}
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funspec.AnyFunSpec
import math.BigDecimal.double2bigDecimal

object HitBoxIntersectionTest:
  private val tolerance: Double = 0.1

  private given distance: Distance = 1.0

  private given equality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(tolerance)

class HitBoxIntersectionTest extends AnyFunSpec :

  import HitBoxIntersectionTest._
  import HitBoxIntersectionTest.given

  describe("An hit box") {

    describe("that is the intersection of other hit boxes") {

      it("should be able to be create from multiple hit box") {
        val hitBox = HitBoxIntersection(
          HitBoxPoint(Point2D(0, 0)),
          HitBoxPoint(Point2D(1, 1)),
          HitBoxEmpty
        )
        assert(hitBox != null)
      }

      describe("that result to be empty ") {

        it("should be able to be create from 0 hit box") {
          val hitBox = HitBoxIntersection()
          assert(hitBox != null)
        }

        it("should be an empty hit box if one of the hit box is empty and the other is a point") {
          val hitBox = HitBoxIntersection(
            HitBoxPoint(Point2D(0, 0)),
            HitBoxEmpty
          )
          assert(hitBox == HitBoxEmpty)
        }

        it("should be an empty hit box if one of the hit box is empty and the other is a rectangle") {
          val hitBox = HitBoxIntersection(
            HitBoxRectangular(Point2D(0, 0), 2, 3, Angle.Degree(0)),
            HitBoxEmpty
          )
          assert(hitBox == HitBoxEmpty)
        }

        it("should be an empty hit box if one of the hit box is empty and the other is a circle") {
          val hitBox = HitBoxIntersection(
            HitBoxCircular(Point2D(0, 0), 2),
            HitBoxEmpty
          )
          assert(hitBox == HitBoxEmpty)
        }

        it("should be an empty hit box if one of the hit box is empty and the others are not") {
          val hitBox = HitBoxIntersection(
            HitBoxPoint(Point2D(0, 0)),
            HitBoxRectangular(Point2D(2, 3), 2, 3, Angle.Degree(0)),
            HitBoxCircular(Point2D(-1, -1), 2),
            HitBoxEmpty
          )
          assert(hitBox == HitBoxEmpty)
        }

        it("should be an empty hit box if there is no intersection between the hit boxes (x)") {
          val hitBox = HitBoxIntersection(
            HitBoxPoint(Point2D(0, 0)),
            HitBoxPoint(Point2D(1, 0))
          )
          assert(hitBox == HitBoxEmpty)
        }

        it("should be an empty hit box if there is no intersection between the hit boxes (y)") {
          val hitBox = HitBoxIntersection(
            HitBoxPoint(Point2D(0, 0)),
            HitBoxPoint(Point2D(0, 1))
          )
          assert(hitBox == HitBoxEmpty)
        }

        it("should be an empty hit box if there is no intersection between the hit boxes same interval") {
          val intervalInterceptPoint = Point2D(1, 1)
          val circle = HitBoxCircular(Point2D(-1, -1), 2)
          val rectangle = HitBoxRectangular(Point2D(2, 2), 2, 2, Angle.Degree(0))
          val hitBox = HitBoxIntersection(
            rectangle,
            circle
          )
          assert(rectangle.contains(intervalInterceptPoint))
          assert(!circle.contains(intervalInterceptPoint))
          assert(hitBox == HitBoxEmpty)
        }

      }

      describe("that result to be a point") {

        it("should be able to be create from 1 hit box") {
          val point = Point2D(0, 0)
          val hitBox = HitBoxIntersection(
            HitBoxPoint(point)
          )
          assert(hitBox.contains(point))
        }

        it("should be able to be create from multiple hit boxes, one is a point") {
          val point = Point2D(0, 0)
          val hitBox = HitBoxIntersection(
            HitBoxPoint(point),
            HitBoxCircular(point, 1)
          )
          assert(hitBox.contains(point))
          assert(hitBox.area.size == 1)
        }

        it("should be able to be create from multiple hit boxes, no one is a point") {
          val pointRectangle = Point2D(2, 0)
          val pointCircle = Point2D(0, 0)
          val hitBox = HitBoxIntersection(
            HitBoxRectangular(pointRectangle, 2, 3, Angle.Degree(0)),
            HitBoxCircular(pointCircle, 1)
          )
          assert(hitBox.contains(Point2D(1, 0)))
          assert(hitBox.area.size == 1)
        }
      }

      describe("that result to be one of given shape") {
        it("should be able to be create from multiple hit boxes, the result is a rectangle") {
          val point = Point2D(0, 0)
          val rectangularHitBox = HitBoxRectangular(point, 2, 2, Angle.Degree(0))
          val hitBox = HitBoxIntersection(
            HitBoxCircular(point, 4),
            rectangularHitBox
          )
          assert(rectangularHitBox.area.forall(hitBox.contains))

        }

        it("should be able to be create from multiple hit boxes, the result is a circle") {
          val point = Point2D(0, 0)
          val circularHitBox = HitBoxCircular(point, 1)
          val hitBox = HitBoxIntersection(
            circularHitBox,
            HitBoxRectangular(point, 2, 2, Angle.Degree(0))
          )
          assert(hitBox.area.forall(circularHitBox.contains))
        }

        it("should be able to be create from multiple hit boxes, the result is a  circle (check interval)") {
          val point = Point2D(0, 0)
          val hitBox = HitBoxIntersection(
            HitBoxCircular(point, 1),
            HitBoxRectangular(point, 2, 2, Angle.Degree(0))
          )
          assert(hitBox.xMax.get === 1.0)
          assert(hitBox.xMin.get === -1.0)
          assert(hitBox.yMax.get === 1.0)
          assert(hitBox.yMin.get === -1.0)
        }
      }

      describe("that result to be a complex shape") {

        val pointRectangle = Point2D(2, 0)
        val pointCircle = Point2D(2, 2)
        val circleHitBox = HitBoxCircular(pointCircle, 1)
        val rectangularHitBox = HitBoxRectangular(pointRectangle, 2, 4, Angle.Degree(0))

        it("should be able to be create from multiple hit boxes, no one is a point") {
          val hitBox = HitBoxIntersection(
            circleHitBox,
            rectangularHitBox
          )
          assert(hitBox != HitBoxEmpty)
        }

        it("should have intersection interval ") {
          val hitBox = HitBoxIntersection(
            circleHitBox,
            rectangularHitBox
          )
          assert(hitBox.xMax.get === 3.0)
          assert(hitBox.xMin.get === 1.0)
          assert(hitBox.yMax.get === 2.0)
          assert(hitBox.yMin.get === 1.0)
        }

        it("shouldn't contains all the points of the hit boxes") {
          val hitBox = HitBoxIntersection(
            circleHitBox,
            rectangularHitBox
          )
          assert(!circleHitBox.area.forall(hitBox.contains))
          assert(!rectangularHitBox.area.forall(hitBox.contains))
        }

        it("should contains the common points of the hit boxes") {
          val hitBox = HitBoxIntersection(
            circleHitBox,
            rectangularHitBox
          )
          for
            x <- hitBox.xMin.get to hitBox.xMax.get by 0.1
            y <- hitBox.yMin.get to hitBox.yMax.get by 0.1
            point = Point2D(x.doubleValue, y.doubleValue)
            if circleHitBox.contains(point) && rectangularHitBox.contains(point)
          yield
            assert(hitBox.contains(point))
        }

        it("shouldn't contains points that aren't in common between the hit boxes") {
          val hitBox = HitBoxIntersection(
            circleHitBox,
            rectangularHitBox
          )
          for
            x <- hitBox.xMin.get to hitBox.xMax.get by 0.1
            y <- hitBox.yMin.get to hitBox.yMax.get by 0.1
            point = Point2D(x.doubleValue, y.doubleValue)
            if !circleHitBox.contains(point) || !rectangularHitBox.contains(point)
          yield
            assert(!hitBox.contains(point))
        }
      }
    }
  }
