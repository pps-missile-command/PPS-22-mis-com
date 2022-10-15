package model.behavior

import model.missile.Timeable

trait Moveable extends Timeable :
  def move(): Moveable
