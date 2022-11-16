import model.collisions.{Affiliation, Collisionable, Damageable, Damager, Collision}
import model.collisions.PimpingByCollisionable._
import model.collisions.PimpingByCollisions._

package object model:

  /**
   * Type alias for the abstraction of virtual time
   */
  type DeltaTime = Double

  /**
   * Type alias for the point that are given in the game when an object is destroyed.
   */
  type ScorePoint = Int

  /**
   * Trait that represents an object that give points when destroyed.
   */
  trait Scorable(val points: ScorePoint) extends Damageable


  extension (actualScore: ScorePoint)

    /**
     * Function that calculate the new score of the player.
     * It only add the points of the destroyed object if the one of the object that inflict damage to the damageable
     * is owned by the player.
     *
     * @param collisions the collisions that have been detected in the game
     * @return the new score of the player.
     */
    def calculateNewScoreBasedOn(collisions: Set[Collision]): ScorePoint =
      (for
        scorableDestroyed <- collisions.allScorableDestroyedThatCollide.toSeq
        if scorableDestroyed.isNotFriendly
        otherElement <- collisions.allElementsThatDestroyed(scorableDestroyed)
        if otherElement.isFriendly
      yield
        scorableDestroyed.getScorePoint)
        .sum + actualScore

