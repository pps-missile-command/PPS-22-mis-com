package model.missile

import model.elements2d.{Angle, Point2D, Vector2D}
import model.missile.zigzag.Direction

import scala.util.Random
import model.missile.zigzag.PimpingByVector2D.-|-
import model.missile.zigzag.defaultMagnitude
import model.missile.zigzag.PimpingByPoint2D.*

package object zigzag:
  /**
   * Case object and class to model the change of direction: Right, Left and a Random one (between right and left)
   */
  sealed class Direction
  case object Right extends Direction
  case object Left extends Direction
  case class Rand(rand: Random) extends Direction

  val signThreshold = 0.5
  val defaultMagnitude = 30



