package view

import controller.Controller
import model.World
import view.gui.GUI
import monix.execution.Scheduler.Implicits.global

object Main:
    def main(args: Array[String]): Unit =
        val swingUI = new GUI(ViewConstants.GUI_width, ViewConstants.GUI_height + 39) //39 is the number of pixels in the bar
        val loop = Controller.start(swingUI)
        loop.runAsyncAndForget