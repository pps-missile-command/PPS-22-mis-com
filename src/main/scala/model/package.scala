import model.collisions.{Affiliation, Collisionable, Damageable}

package object model:

  /**
   * Type alias for the point that are given in the game when an object is destroyed.
   */
  type ScorePoint = Int

  /**
   * Trait that represents an object that give points when destroyed.
   */
  trait Scorable(val points: ScorePoint) extends Damageable

  /**
   * Function that calculate the new score of the player.
   *
   * @param collisionables list of collisionable objects.
   * @param actualScore    the actual score of the player.
   * @return the new score of the player.
   */
  def calculateNewScore(collisionables: List[Collisionable], actualScore: ScorePoint): ScorePoint =
    def getScorePoint(collisionable: Collisionable): ScorePoint =
      collisionable match
        case scorable: Scorable if scorable.isDestroyed && scorable.affiliation != Affiliation.Friendly => scorable.points
        case _ => 0

    collisionables.map(getScorePoint).sum + actualScore
