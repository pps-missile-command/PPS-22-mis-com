package model.behavior

import model.behavior.Timeable

trait Moveable extends Timeable :
  def move(): Moveable
