package view.gui

import model.elements2d.Point2D

import java.awt.Component
import monix.execution.cancelables.SingleAssignCancelable
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.subjects.PublishSubject

import java.awt.event.{MouseAdapter, MouseEvent, MouseMotionListener}

/**
 * Extension method that adds a new mouseObservable method to [[Component]].
 * This method returns an Observable[Point2D] that adds a mouse clicked listener to the given
 * component and generating new events as the click are listened
 */
extension (component: Component)
  def mouseObservable(): Observable[Point2D] =
    Observable.create(OverflowStrategy.Unbounded) { subject =>
      component.addMouseListener(new MouseAdapter:
        override def mouseClicked(e: MouseEvent): Unit =
          subject.onNext(Point2D(e.getX, e.getY))
      )
      SingleAssignCancelable()
    }
