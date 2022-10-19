package view.gui

import controller.Event
import model.World
import monix.eval.Task
import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject
import org.w3c.dom.events.MouseEvent
import view.gui.UI

import java.awt.event.MouseMotionListener
import java.awt.{Color, Graphics, event}
import javax.swing.*

class GUI(width: Int, height: Int) extends UI:
    private val frame = JFrame("Game")
    frame.setSize(width, height)
    frame.setVisible(true)
    frame.setLocationRelativeTo(null)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    customRender(World.initialWorld)

    def render(world: World): Task[Unit] = Task {
        SwingUtilities.invokeAndWait { () =>
            if (frame.getContentPane.getComponentCount != 0)
                frame.getContentPane.remove(0)
            frame.getContentPane.add(WorldPane(world, width, height))
            frame.getContentPane.repaint()
        }
    }

    def customRender(world: World) =
        if (frame.getContentPane.getComponentCount != 0)
            frame.getContentPane.remove(0)
        frame.getContentPane.add(WorldPane(world, width, height))
        frame.getContentPane.repaint()


    override def events: Observable[Event] = ???

    override def gameOver: Task[Unit] = ???