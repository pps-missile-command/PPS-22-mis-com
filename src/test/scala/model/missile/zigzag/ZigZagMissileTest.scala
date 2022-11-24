package model.missile.zigzag

import model.DeltaTime
import model.elements2d.Point2D
import model.missile.{Missile, zigzag}
import model.missile.zigzag.ZigZagMissile.*
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.matchers.should.Matchers.shouldBe

import scala.util.Random

object ZigZagMissileTest:
  val maxWidth: Double = 20

class ZigZagMissileTest extends AnyFunSpec :
  import ZigZagMissileTest._
  given Random()

  describe("A missile with zigzag behavior") {
    it("should be a normal missile") {
      val missile = ZigZagMissile(Point2D(0,0), Point2D(10,10), 5, maxWidth = maxWidth)
      missile shouldBe a[Missile]
    }
    it("should follow the sequence of subpoints") {
      val missile: Missile = ZigZagMissile(Point2D(0,0), Point2D(10,10), 5, maxWidth = maxWidth)
      val distanceToMove = missile.position <-> missile.destination
      val timeToMove: DeltaTime = distanceToMove / missile.velocity
      val newMissile = missile.timeElapsed(timeToMove).move()
      assert(newMissile.asInstanceOf[ZigZagMissile].subDestinationReached)
      assert(!newMissile.asInstanceOf[ZigZagMissile].destinationReached)
    }
    it("should explode only in the final position") {
      val missile: Missile = ZigZagMissile(Point2D(0,0), Point2D(10,10), 2, maxWidth = maxWidth)
      val newMissile: Missile = missile.timeElapsed((missile.position <-> missile.destination) / missile.velocity).move().asInstanceOf[Missile]
      println(newMissile.destination)
      println(newMissile.position)
      val tempMissile: Missile = newMissile.timeElapsed(0).move().asInstanceOf[Missile]
      val finalMissile = tempMissile.timeElapsed((tempMissile.position <-> tempMissile.destination) / tempMissile.velocity).move()
      assert(finalMissile.asInstanceOf[ZigZagMissile].subDestinationReached)
      assert(finalMissile.asInstanceOf[ZigZagMissile].destinationReached)
    }
  }