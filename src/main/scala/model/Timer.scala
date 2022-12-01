package model

import model.behavior.Timeable

/**
 * Object that represents the global timer, used to measure
 * the survival time of the player
 */
object Timer:
  /**
   * Case class that models the Timer, encapsulating the current value
   * of the timer
   * @param time
   */
  case class Timer(time: DeltaTime) extends Timeable:
    /**
     * Method that updates the current value of the timer
     * @param dt This is the virtual delta time thas has been passed since the last update
     *  @return the new Timeable object with the current virtual time updated
     */
    override def timeElapsed(dt: DeltaTime): Timer = this.copy(time + dt)