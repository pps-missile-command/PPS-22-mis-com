package model.behavior

import model.elements2d.Point2D
import model.DeltaTime

/**
 * Trait that models whatever entity that is able to move into the game.
 * This trait extends [[Timeable]] trait because a moveable entity moves as the time passes.
 */
trait Moveable extends Timeable :
  /**
   * This function creates a new Moveable object with a new virtual time based on the delta time passed
   * @param dt the delta time passed as the game go on
   * @return the new Moveable object
   */
  override def timeElapsed(dt: DeltaTime): Moveable

  /**
   * This method moves the object through the world of the game
   * @return the new object moved, with its position updated
   */
  def move(): Moveable

  /**
   * This method returns whether the destination of the Moveable object has been reached or not
   * @return true if the destination has been reached, false otherwise
   */
  def destinationReached: Boolean

  /**
   * Current position of the Moveable object
   * @return the current position into the world
   */
  def position: Point2D

  /**
   * The final destination of the Moveable object
   * @return the final destination to which to move
   */
  def destination: Point2D

