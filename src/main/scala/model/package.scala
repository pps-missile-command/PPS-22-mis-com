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
   * It only add the points of the destroyed object if the one of the object that inflict damage to the damageable 
   * is owned by the player.
   *
   * @param collisionables a map that has as keys the damageable objects and as values a list with the damaeger that collides with them.
   * @param actualScore    the actual score of the player.
   * @return the new score of the player.
   */
  def calculateNewScore(collisionables: Map[Collisionable, List[Collisionable]], actualScore: ScorePoint): ScorePoint =
    def getScorePoint(collisionable: Collisionable, damagers: List[Collisionable]): ScorePoint =
      collisionable match
        case scorable: Scorable if scorable.isDestroyed && damagers.map(_.affiliation).contains(Affiliation.Friendly) => scorable.points
        case _ => 0

    collisionables.map((damageable, damagers) => getScorePoint(damageable, damagers)).sum + actualScore
