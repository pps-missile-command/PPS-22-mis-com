package model.elements2d

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import model.elements2d._
import org.scalactic.Tolerance.convertNumericToPlusOrMinusWrapper

object AngleTest:
  private val degreeTestAngle = 45
  private val radiantTestAngle = math.Pi / 4
  private val degreeStraightAngle = 180
  private val radiantStraightAngle = math.Pi

class AngleTest extends AnyFunSpec :

  import AngleTest._

  describe("An Angle") {
    describe("in degree") {
      it("should be created from a value in degree") {
        val angle = Angle.Degree(degreeTestAngle)
        assert(angle.degree == degreeTestAngle)
      }
      it("should be converted to radian") {
        val angle = Angle.Degree(degreeTestAngle)
        assert(angle.radiant == radiantTestAngle)
      }
      it("should be negative after 180 degree") {
        val angle = Angle.Degree(degreeStraightAngle + degreeTestAngle)
        assert(angle.degree == -degreeStraightAngle + degreeTestAngle)
        assert(angle.radiant == -radiantStraightAngle + radiantTestAngle)
      }
      it("should be 180° at -180°") {
        val angle = Angle.Degree(-degreeStraightAngle)
        assert(angle.degree == degreeStraightAngle)
      }
      it("should be pi at -180°") {
        val angle = Angle.Degree(-degreeStraightAngle)
        assert(angle.radiant === radiantStraightAngle)
      }
    }
    describe("in radian") {
      it("should be created from a value in radian") {
        val angle = Angle.Radian(radiantTestAngle)
        assert(angle.radiant === radiantTestAngle)
      }
      it("should be converted to degree") {
        val angle = Angle.Radian(radiantTestAngle)
        assert(angle.degree == degreeTestAngle)
      }
      it("should be negative after pi degree") {
        val angle = Angle.Radian(-radiantStraightAngle - radiantTestAngle)
        assert(angle.degree == -degreeStraightAngle + degreeTestAngle)
        assert(angle.radiant == -radiantStraightAngle + radiantTestAngle)
      }
      it("should be 180° at -pi") {
        val angle = Angle.Radian(-radiantStraightAngle)
        assert(angle.degree == degreeStraightAngle)
      }
      it("should be pi at -pi") {
        val angle = Angle.Radian(-radiantStraightAngle)
        assert(angle.radiant === radiantStraightAngle)
      }
    }
  }
