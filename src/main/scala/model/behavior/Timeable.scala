package model.behavior

import model.DeltaTime

trait Timeable:

  def timeElapsed(dt: DeltaTime): Timeable
