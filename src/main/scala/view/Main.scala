package view

import controller.GameLoop
import model.World
import view.gui.GUI
import monix.execution.Scheduler.Implicits.global

object Main:
    @main def startGame(): Unit =
        val swingUI = new GUI(ViewConstants.GUI_width, ViewConstants.GUI_height + 39) //39 is the number of pixels in the bar
        val loop = GameLoop.start(swingUI)
        loop.runAsyncAndForget