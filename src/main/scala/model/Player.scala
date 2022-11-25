package model

import model.Timer.Timer
import model.collisions.Collision

/**
 * Trait that represents a player in the game.
 *
 */
trait Player extends PlayerActions[Player] :

  /**
   * The score of the player.
   */
  val score: ScorePoint

  /**
   * The timer of the player.
   */
  val timer: Timer

  def updateScore(score: ScorePoint): Player

  def updateTimer(timer: Timer): Player

/**
 * Companion object of the Player class.
 */
object Player:

  /**
   * The initial player of the game with a score of 0 and a timer of 0.
   *
   * @return the initial player
   */
  def initialPlayer: Player =
    Player(0, Timer(0))

  /**
   * Creates a new player with the given score and timer.
   *
   * @param score the score of the player
   * @param timer the survival timer of the player
   * @return a new player with the given score and timer
   */
  def apply(score: ScorePoint, timer: Timer): Player =
    case class PlayerImpl(score: ScorePoint, timer: Timer) extends Player :
      override def updateScore(score: ScorePoint): Player =
        PlayerImpl(score, timer)

      override def timeElapsed(dt: DeltaTime): Player =
        updateTimer(timer.timeElapsed(dt))

      override def updateScoreByCollisions(collisions: Set[Collision]): Player =
        updateScore(score calculateNewScoreBasedOn collisions)

      override def updateTimer(timer: Timer): Player =
        PlayerImpl(score, timer)

    PlayerImpl(score, timer)
