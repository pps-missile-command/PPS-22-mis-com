package model.elements2d

import scala.annotation.targetName

enum Vector2D:
  @targetName("vector2D")
  case <>(xComponent: Double, yComponent: Double)
  case Zero

  def x: Double = this match
    case <>(x, _) => x
    case Zero => 0

  def y: Double = this match
    case <>(_, y) => y
    case Zero => 0

  def magnitude: Double = this match
    case <>(x, y) => math.sqrt(x * x + y * y)
    case Zero => 0

  def direction: Angle = this match
    case <>(x, y) => Angle.Radian(math.atan2(y, x))
    case Zero => Angle.Degree(0)

  @targetName("plus")
  def +(other: Vector2D): Vector2D = <>(x + other.x, y + other.y) match
    case <>(0, 0) => Zero
    case vector => vector

  @targetName("minus")
  def -(other: Vector2D): Vector2D = <>(x - other.x, y - other.y) match
    case <>(0, 0) => Zero
    case vector => vector

  @targetName("multiplyByScalar")
  def *(scalar: Double): Vector2D = <>(x * scalar, y * scalar) match
    case <>(0, 0) => Zero
    case vector => vector

  @targetName("divideByScalar")
  def /(scalar: Double): Vector2D = this * (1 / scalar)

  def normalize: Vector2D = this match
    case Zero => Zero
    case vector => vector / magnitude

  @targetName("opposite")
  def unary_- = this * -1

object Vector2D:
  def apply(x: Double, y: Double): Vector2D = (x, y) match
    case (0, 0) => Zero
    case _ => <>(x, y)

  def apply(magnitude: Double, direction: Angle): Vector2D = magnitude match
    case 0 => Zero
    case _ => <>(math.cos(direction.radiant), math.sin(direction.radiant)) * magnitude

