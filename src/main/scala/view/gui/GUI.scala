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

import java.awt.event.MouseMotionListener
import java.awt.{Color, Graphics, event}
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
      .map((x, y) => (x / width.toDouble, y / height.toDouble))
      .map((x, y) => Event.LaunchMissileTo(Point2D(x, y)) )

    override def gameOver: Task[Unit] = Task {
        SwingUtilities.invokeAndWait { () =>
            if (frame.getContentPane.getComponentCount != 0)
                0 to (frame.getContentPane.getComponentCount - 1) foreach { i=>
                    frame.getContentPane.remove(i)
                }
            frame.getContentPane.add(EndGamePane(width, height))
            frame.getContentPane.repaint()
        }
    }