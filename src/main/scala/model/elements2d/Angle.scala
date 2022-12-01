package model.elements2d

/**
 * Companion object for the class [[Angle]] that contains the constants.
 */
object Angle:
  private val straightAngleRadiant = math.Pi
  private val straightAngleDegree = 180

  private def identity(value: Double): Double = value

  private def periodicWithNegative(value: Double, straightAngle: Double): Double = -straightAngle + math.abs(value % straightAngle)

  private def periodicCircular(value: Double, completeAngle: Double): Double = value % completeAngle

/**
 * Represents an angle that could be measured in either degree or radiant.
 *
 */
enum Angle:

  import Angle._

  /**
   * Creates an angle from a given value in degree.
   *
   * @param value the value of the angle in degree
   */
  case Degree(value: Double)

  /**
   * Creates an angle from a given value in radiant.
   *
   * @param value the value of the angle in radiant
   */
  case Radian(value: Double)

  /**
   * Returns the value of the angle in radiant.
   *
   * @return the value of the angle in radiant
   */
  def radiant: Double =
    def degreeToRadian(value: Double): Double = value * straightAngleRadiant / straightAngleDegree

    conversion(straightAngleRadiant)(degreeToRadian)(identity)

  /**
   * Returns the value of the angle in degree.
   *
   * @return the value of the angle in degree
   */
  def degree: Double =
    def radiantToDegree(value: Double): Double = value * straightAngleDegree / straightAngleRadiant

    conversion(straightAngleDegree)(identity)(radiantToDegree)

  private def conversion(straightAngleOfConversion: Double)(fromDegree: Double => Double)(fromRadian: Double => Double): Double =
    def conversionPeriodicWithNegative(value: Double)(straightAngleOfConverted: Double)(conversionFunction: Double => Double): Double =
      val completeAngleOfConverted = 2 * straightAngleOfConverted
      val valuePeriodicCircular = periodicCircular(value, completeAngleOfConverted)
      value match
        case _ if valuePeriodicCircular == -straightAngleOfConverted => straightAngleOfConversion
        case _ if valuePeriodicCircular > straightAngleOfConverted || valuePeriodicCircular < -straightAngleOfConverted =>
          periodicWithNegative(conversionFunction(value), straightAngleOfConversion)
        case _ => conversionFunction(valuePeriodicCircular)

    this match
      case Degree(value) => conversionPeriodicWithNegative(value)(straightAngleDegree)(fromDegree)
      case Radian(value) => conversionPeriodicWithNegative(value)(straightAngleRadiant)(fromRadian)
