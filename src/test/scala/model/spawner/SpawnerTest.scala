package model.spawner
import model.collisions.Affiliation
import model.elements2d.Point2D
import model.missile.Missile
import model.spawner.GenericSpawnerTest.interval
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

object SpawnerTest:
  val maxHeight = 500
  val maxWidth = 300

class SpawnerTest extends AnyFunSpec :
  import SpawnerTest._
  
  given Random()
  
  
  
