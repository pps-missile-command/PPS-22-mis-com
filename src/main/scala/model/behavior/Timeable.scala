package model.behavior

import model.DeltaTime

/**
 * This trait models whatever object that needs to represent the passing of the time in a virtual sense (virtual time).
 * In fact all entity that incapsulate a meaning of time should implements this interface.
 */
trait Timeable:

  /**
   * This function models the passing of the time as the game go on
   * @param dt This is the virtual delta time thas has been passed since the last update
   * @return the new Timeable object with the current virtual time updated
   */
  def timeElapsed(dt: DeltaTime): Timeable
