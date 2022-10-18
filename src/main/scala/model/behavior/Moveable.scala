package model.behavior

import model.elements2d.Point2D
import model.DeltaTime

trait Moveable extends Timeable :
  override def timeElapsed(dt: DeltaTime): Moveable
  def move(): Moveable
  def destinationReached: Boolean
  def position: Point2D
  def destination: Point2D

