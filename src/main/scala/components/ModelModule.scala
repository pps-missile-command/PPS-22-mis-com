package components

import model.Game

object ModelModule:
  trait Model:
    def initializeGame(): Game
  trait Provider:
    val model: Model
  trait Component:
    class ModelImpl extends Model:
      override def initializeGame(): Game = Game.initialGame
  trait Interface extends Provider with Component