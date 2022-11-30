package view.gui

import view.Visualizer

import java.awt.{Color, Component, Graphics, FlowLayout}
import javax.swing.{JLabel, JPanel}
import javax.swing.JButton

import java.awt.BorderLayout
import java.awt.Dimension

/**
 * Class that represent the end game panel, used to draw the final graphic interface to allow
 * the user to restart the game and read the score and the survive time reached
 * @param width The width of the panel
 * @param height The height of the panel
 */
class EndGamePane(width: Int, height: Int) extends JPanel{

  this.setSize(width, height)

  override def paintComponent(graphics: Graphics): Unit =
    super.paintComponent(graphics)
    graphics.clearRect(0, 0, width, height)
    graphics.setColor(Color.GRAY)
    graphics.fillRect((width / 2) - 50, (height / 2) - 50, 100, 100)
    graphics.setColor(Color.WHITE)
    graphics.drawString("FINE GIOCO", width/2 - 30, height/2)

}
