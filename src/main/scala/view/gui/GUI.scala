package view.gui

import controller.{Controller, Event}
import monix.eval.Task
import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject
import org.w3c.dom.events.MouseEvent
import view.gui.UI
import model.elements2d.Point2D
import controller.Event
import model.{Game, World}
import view.{Main, ViewConstants}
import view.gui.WorldPane

import java.awt.event.MouseMotionListener
import java.awt.{BorderLayout, Color, Dimension, FlowLayout, Graphics, event}
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class GUI(width: Int, height: Int) extends UI:
    private val frame = JFrame("Game")

    frame.setSize(width, height)
    frame.setVisible(true)
    frame.setLocationRelativeTo(null)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

    def render(game: Game): Task[Unit] = Task {
        SwingUtilities.invokeLater { () =>
            if (frame.getContentPane.getComponentCount != 0)
                frame.getContentPane.remove(0)
            frame.getContentPane.add(WorldPane(game, width, height))
            frame.getContentPane.repaint()
        }
    }

    override def events: Observable[Event] = frame.getContentPane
      .mouseObservable()
      .map(p => Event.LaunchMissileTo(Point2D((p.x.toDouble * World.width.toDouble) / ViewConstants.GUI_width.toDouble, (p.y.toDouble * World.height.toDouble) / ViewConstants.GUI_height.toDouble )) )

    override def gameOver(game: Game): Task[Unit] = Task {
        SwingUtilities.invokeLater { () =>
            if (frame.getContentPane.getComponentCount != 0)
                frame.getContentPane.remove(0)
            val panel = new JPanel()
            panel.setSize(width, height)
            panel.setLayout(new FlowLayout())
            val button = new JButton("RIGIOCA")
            val score = new JLabel(s"SCORE FINALE: ${game.player.score}")
            val time = new JLabel(s"TEMPO DI GIOCO: ${game.player.timer.time}")
            button.addActionListener(new ActionListener() {
                override def actionPerformed(e: ActionEvent): Unit = {
                    frame.dispose()
                    Main.main(null)
                }
            })
            panel.add(button)
            panel.add(score)
            panel.add(time)

            frame.getContentPane.add(panel)
            frame.revalidate()
            frame.repaint()
        }
    }