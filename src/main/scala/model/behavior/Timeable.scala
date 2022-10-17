package model.behavior

import model.missile.DeltaTime

trait Timeable:

  def timeElapsed(dt: DeltaTime): Timeable
