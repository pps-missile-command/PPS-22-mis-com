package model

import model.behavior.Timeable

object Timer:

  case class Timer(time: DeltaTime) extends Timeable:

    override def timeElapsed(dt: DeltaTime): Timer = this.copy(time + dt)