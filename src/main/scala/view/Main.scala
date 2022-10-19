package view

import view.gui.GUI

object Main:
    @main def startGame(): Unit =
        val swingUI = new GUI(ViewConstants.GUI_width, ViewConstants.GUI_height)
