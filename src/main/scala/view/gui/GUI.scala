package view.gui

import components.ViewModule.View
import controller.Event
import monix.eval.Task
import monix.reactive.{Observable, OverflowStrategy}
import model.elements2d.Point2D
import model.{Game, World}
import view.{Main, ViewConstants}
import view.gui.WorldPane
import java.awt.{BorderLayout, Color, Dimension, FlowLayout, Graphics, event}
import javax.swing.*

/**
 * Class that represent the main interface that encapsulate the GUI events and the
 * panels change, based on the event observed. This also manage the render function to visualize all the
 * game's elements.
 * @param width The width of the frame
 * @param height The height of the frame
 */
class GUI(width: Int, height: Int) extends View :
  given OverflowStrategy[Event] = OverflowStrategy.Default

  private val frame = JFrame("Game")
  private val button = JButton()

  /**
   * Method that takes the grame obserable and map all the Point2D elements emitted due to the
   * mouse click event into a [[Event.LaunchMissileTo]] events.
   */
  private val gameEvent: Observable[Event] =
    frame
      .getContentPane
      .mouseObservable()
      .map(p =>
        Event
          .LaunchMissileTo(
            Point2D(
              (p.x * World.width.toDouble) / ViewConstants.GUI_width.toDouble,
              (p.y * World.height.toDouble) / ViewConstants.GUI_height.toDouble
            )
          )
      )
  /**
   * Method that takes the button obserable and map all the empty events emitted
   * into StartGame events.
   */
  private val startEvent: Observable[Event] =
    button
      .clickObservable()
      .map(_ => Event.StartGame)

  frame.setSize(width, height)
  frame.setVisible(true)
  frame.setLocationRelativeTo(null)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

  /**
   *
   *  @return an observable of events
   */
  override def events: Observable[Event] =
    Observable(gameEvent, startEvent).merge

  /**
   *
   *  @return a task that will create the view
   */
  override def initialGame(): Task[Unit] = Task {
    SwingUtilities.invokeLater { () =>
      clearPanel()
      val panel = new JPanel()
      panel.setSize(width, height)
      panel.setLayout(new FlowLayout())
      button.setText("AVVIA PARTITA")
      panel.add(button)

      frame.getContentPane.add(panel)
      frame.revalidate()
      frame.repaint()
    }
  }

  /**
   *
   * @param game the game to render
   *  @return a task that will render the world
   */
  override def render(game: Game): Task[Unit] = Task {
    SwingUtilities.invokeLater { () =>
      clearPanel()
      frame.getContentPane.add(WorldPane(game, width, height))
      frame.getContentPane.repaint()
    }
  }

  /**
   *
   * @param game the game to render at the end
   *  @return a task that will render the end of the game
   */
  override def gameOver(game: Game): Task[Unit] = Task {
    SwingUtilities.invokeLater { () =>
      clearPanel()
      val panel = new EndGamePane(width, height)
      panel.setLayout(new FlowLayout())
      button.setText("RIGIOCA")
      val score = new JLabel(s"SCORE FINALE: ${game.player.score}")
      val time = new JLabel(s"TEMPO DI GIOCO: ${game.player.timer.time}")
      panel.add(button)
      panel.add(score)
      panel.add(time)
      frame.getContentPane.add(panel)
      frame.revalidate()
      frame.repaint()
    }
  }

  /**
   * Method that clear the frame panel from everything previously added.
   */
  private def clearPanel(): Unit =
    if (frame.getContentPane.getComponentCount != 0)
      frame.getContentPane.remove(0)