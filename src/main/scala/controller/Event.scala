package controller

import model.elements2d.Point2D

/**
 * Enum of the possible events in the game.
 */
enum Event:
  /**
   * Class that represents the event of passing time.
   *
   * @param time the time passed
   */
  case TimePassed(deltaTime: Double)

  /**
   * Class that represents the event of launching a friendly missile.
   *
   * @param position the ending position of the missile
   */
  case LaunchMissileTo(position: Point2D)
