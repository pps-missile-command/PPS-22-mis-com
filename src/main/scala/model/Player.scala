package model

import model.Timer.Timer

/**
 * Class that represents a player in the game.
 *
 * @param score the score of the player
 * @param timer the surviving timer of the player
 */
case class Player(score: ScorePoint, timer: Timer)

/**
 * Companion object of the Player class.
 */
object Player:

  /**
   * The initial player.
   *
   * @return the initial player
   */
  def initialPlayer: Player =
    Player(0, Timer(0))
