package view.gui

import java.awt.Component
import monix.execution.cancelables.SingleAssignCancelable
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.subjects.PublishSubject

import java.awt.event.{MouseAdapter, MouseEvent, MouseMotionListener}

extension (component: Component)
  def mouseObservable(): Observable[(Int, Int)] =
    Observable.create(OverflowStrategy.Unbounded) { subject =>
      component.addMouseListener(new MouseAdapter:
        override def mouseClicked(e: MouseEvent): Unit =
          subject.onNext((e.getX, e.getY))
      )
      SingleAssignCancelable()
    }
