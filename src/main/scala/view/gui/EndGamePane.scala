package view.gui

import view.Visualizer

import java.awt.Graphics
import javax.swing.JPanel

class EndGamePane(width: Int, height: Int) extends JPanel{

  this.setSize(width, height)

  override def paintComponent(graphics: Graphics): Unit =
    super.paintComponent(graphics)
    graphics.clearRect(0, 0, width, height)
    graphics.drawRect((width / 2) - 50, (height / 2) - 50, 500, 500)

}
