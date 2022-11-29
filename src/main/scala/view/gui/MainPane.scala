package view.gui

import java.awt.FlowLayout
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JButton, JFrame, JPanel, SwingUtilities, WindowConstants}

class MainPane(start: () => _, width: Int, height: Int) extends JFrame:

  this.setSize(width, height)
  this.setVisible(true)
  this.setLocationRelativeTo(null)
  this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

  this.initialPage()

  def initialPage(): Unit =
    SwingUtilities.invokeLater { () =>
      if (this.getContentPane.getComponentCount != 0)
        this.getContentPane.remove(0)
      val panel = new JPanel()
      panel.setSize(width, height)
      panel.setLayout(new FlowLayout())
      val button = new JButton("AVVIA PARTITA")
      button.addActionListener(new ActionListener() {
        override def actionPerformed(e: ActionEvent): Unit = {
          dispose()
          start()
        }
      })
      panel.add(button)

      this.getContentPane.add(panel)
      this.revalidate()
      this.repaint()
    }
