package model.collisions

import model.collisions.hitbox.HitBoxEmpty
import model.elements2d.Point2D
import org.scalatest.funspec.AnyFunSpec

class HitBoxEmptyTest extends AnyFunSpec :

  private val hitBoxEmpty = HitBoxEmpty

  describe("An hit box") {

    describe("that is empty") {

      it("should have an empty iterator of points") {
        assert(!hitBoxEmpty.area.hasNext)
      }

      it("should not contain any point") {
        assert(!hitBoxEmpty.contains(Point2D(0, 0)))
      }

      it("should have a size of 0") {
        assert(hitBoxEmpty.area.isEmpty)
      }

      it("shouldn't have a space of points ( no x and y interval)") {
        assert(hitBoxEmpty.xMax == Option.empty)
        assert(hitBoxEmpty.xMin == Option.empty)
        assert(hitBoxEmpty.yMax == Option.empty)
        assert(hitBoxEmpty.yMin == Option.empty)
      }
    }
  }
