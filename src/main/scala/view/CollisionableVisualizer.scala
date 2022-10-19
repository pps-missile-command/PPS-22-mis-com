package view

import javafx.scene.image.{Image, ImageView}
import model.collisions.Collisionable
import model.missile.{ Missile, MissileImpl, hitboxBase, hitboxHeight}

//case class CollisionableElement(imageView: ImageView, x: Double, y: Double)
//
//object MissileView:
//  val resourceFolderPath: String = ???
//
//  val conversion: Collisionable => CollisionableElement = _ match
//    case m: Missile => CollisionableElement(
//      ImageView(Image(getClass.getResourceAsStream(""), hitboxBase, hitboxHeight, false, false)),
//      m.position.x,
//      m.position.y)
//
//  def apply(imageView: ImageView, x: Double, y: Double) =
//    CollisionableElement(imageView, x, y)
//
//  def printMissiles(collisionables: List[Collisionable]): List[CollisionableElement] =
//    for
//      i <- collisionables
//    yield conversion(i)
//
