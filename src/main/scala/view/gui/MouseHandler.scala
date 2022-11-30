package view.gui

import model.elements2d.Point2D

import java.awt.Component
import monix.execution.cancelables.SingleAssignCancelable
import monix.reactive.{Observable, OverflowStrategy}

import java.awt.event.{MouseAdapter, MouseEvent, MouseMotionListener}

/**
 * Extension method that adds a new mouseObservable method to [[Component]].
 * This method returns an Observable[Point2D] that adds a mouse clicked listener to the given
 * component and generating new events as the click are listened
 */
extension (component: Component)
  def mouseObservable(): Observable[Point2D] =
    Observable.create(OverflowStrategy.Unbounded) { subject =>
      component.addMouseListener(new MouseAdapter :
        override def mouseClicked(e: MouseEvent): Unit =
          subject.onNext(Point2D(e.getX, e.getY))
      )
      SingleAssignCancelable()
    }

extension (button: javax.swing.JButton)

  /**
   * Extension method that emits a new event every time the [[javax.swing.JButton]] is clicked.
   *
   * @return an Observable[Unit] that emits a new event every time the button is clicked
   */
  def clickObservable(): Observable[Unit] =
    Observable.create(OverflowStrategy.Unbounded) { subject =>
      button.addActionListener(
        _ =>
          subject.onNext(())
      )
      SingleAssignCancelable()
    }
