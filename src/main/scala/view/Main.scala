package view

import model.World
import view.gui.GUI

object Main:
    @main def startGame(): Unit =
        val swingUI = new GUI(ViewConstants.GUI_width, ViewConstants.GUI_height + 39) //39 is the number of pixels in the bar
        swingUI.customRender(World.initialWorld)