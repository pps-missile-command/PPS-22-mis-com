package model.elements2d

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import model.elements2d._

class AngleTest extends AnyFunSpec:
    describe("An Angle") {
      describe("in degree") {
        it("should be create from a value in degree") {
          val angle = Angle.Degree(45)
          assert(angle.degree == 45)
        }
        it("should be converted to radian") {
          val angle = Angle.Degree(90)
          assert(angle.radiant == math.Pi / 2)
        }
        it("should be negative after 180 degree") {
          val angle = Angle.Degree(225)
          assert(angle.degree == -135)
          assert(angle.radiant == -math.Pi / 4 * 3)
        }
      }
      describe("in radian") {
        it("should be create from a value in radian") {
          val angle = Angle.Radian(math.Pi / 2)
          assert(angle.radiant == math.Pi / 2)
        }
        it("should be converted to degree") {
          val angle = Angle.Radian(math.Pi / 4)
          assert(angle.degree == 45)
        }
        it("should be negative after pi degree") {
          val angle = Angle.Radian(math.Pi / 4 * 5)
          assert(angle.degree == -135)
          assert(angle.radiant == -math.Pi / 4 * 3)
        }
      }
    }
