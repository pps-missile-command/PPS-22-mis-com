package model.elements2d

import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop.forAll

object AngleTestProperties:
  private val degreeStraightAngle = 180.0
  private val radiantStraightAngle = math.Pi
  private val degreeLimitAngle = 4 * degreeStraightAngle
  private val radiantLimitAngle = 4 * radiantStraightAngle
  private val angleRadiantGen = Gen.choose(-radiantLimitAngle, radiantLimitAngle).map(Angle.Radian.apply)
  private val angleDegreeGen = Gen.choose(-degreeLimitAngle, degreeLimitAngle).map(Angle.Degree.apply)


class AngleTestProperties extends Properties("Angle") :

  import AngleTestProperties._

  property("Angle in degree should be in interval ]-180°, 180°] (Degree)") = forAll(angleDegreeGen) { angle =>
    angle.degree > -degreeStraightAngle && angle.degree <= degreeStraightAngle
  }

  property("Angle in degree should be in interval ]-180°, 180°] (Radiant)") = forAll(angleRadiantGen) { angle =>
    angle.degree > -degreeStraightAngle && angle.degree <= degreeStraightAngle
  }

  property("Angle in radiant should be in interval ]-pi, pi] (Degree)") = forAll(angleDegreeGen) { angle =>
    angle.radiant > -radiantStraightAngle && angle.radiant <= radiantStraightAngle
  }

  property("Angle in radiant should be in interval ]-pi, pi] (Radiant)") = forAll(angleRadiantGen) { angle =>
    angle.radiant > -radiantStraightAngle && angle.radiant <= radiantStraightAngle
  }
