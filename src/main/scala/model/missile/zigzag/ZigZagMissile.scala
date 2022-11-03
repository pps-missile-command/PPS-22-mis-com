package model.missile.zigzag
import model.collisions.Affiliation
import model.elements2d.Point2D
import model.missile.*

import scala.util.Random

object ZigZagMissile:

  trait ZigZagMissile:
    missile: Missile =>
      def zigzag(): Boolean = true

    override def move(): Missile =
      println("OVERRIDE")
      this

  def apply() = new MissileImpl(5, Point2D(0,0), Point2D(5,5)) with ZigZagMissile

@main def test() =
  import ZigZagMissile.*
  given random: Random()
  val zigzagMissile = apply()
  zigzagMissile.zigzag()
  zigzagMissile.move()

