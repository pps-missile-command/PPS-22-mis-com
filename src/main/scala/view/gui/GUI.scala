package view.gui

import controller.Event
import model.World
import monix.eval.Task
import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject
import org.w3c.dom.events.MouseEvent
import view.gui.UI
import model.elements2d.Point2D
import controller.Event
import view.Main

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

    def render(world: World): Task[Unit] = Task {
        SwingUtilities.invokeAndWait { () =>
            if (frame.getContentPane.getComponentCount != 0)
                frame.getContentPane.remove(0)
            frame.getContentPane.add(WorldPane(world, width, height))
            frame.getContentPane.repaint()
        }
    }

    def customRender(world: World) =
        SwingUtilities.invokeAndWait { () =>
            if (frame.getContentPane.getComponentCount != 0)
                frame.getContentPane.remove(0)
            frame.getContentPane.add(WorldPane(world, width, height))
            frame.getContentPane.repaint()
        }


    override def events: Observable[Event] = frame.getContentPane
      .mouseObservable()
      .map((x, y) => Event.LaunchMissileTo(Point2D(x, y)) )

    override def gameOver: Task[Unit] = Task {
        println("hello1")
        SwingUtilities.invokeAndWait { () =>
            if (frame.getContentPane.getComponentCount != 0)
                frame.getContentPane.remove(0)
            val panel = new JPanel()
            panel.setSize(width, height)
            panel.setLayout(new FlowLayout())
            val button = new JButton("RIGIOCA")

            button.addActionListener(new ActionListener() {
                override def actionPerformed(e: ActionEvent): Unit = {
                    frame.dispose()
                    Main.startGame()
                }
            })
            button.setText("RIGIOCA")
            panel.add(button)

            frame.getContentPane.add(panel)
            frame.getContentPane.repaint()
            frame.revalidate()
            frame.repaint()
            println("hello2")
        }
    }
    
//    def gameOverRender: Task[Unit] = Task {
//        println("hello1")
//        SwingUtilities.invokeAndWait { () =>
//            if (frame.getContentPane.getComponentCount != 0)
//                frame.getContentPane.remove(0)
//            val panel = new JPanel()
//            panel.setSize(width, height)
//            panel.setLayout(new FlowLayout())
//            val button = new JButton()
//            button.setText("RIGIOCA")
//            panel.add(button)
//            frame.getContentPane.add(panel)
//            frame.getContentPane.repaint()
//            println("hello2")
//        }
//    }
