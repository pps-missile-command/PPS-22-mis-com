package controller

import monix.reactive.Observable
import scala.concurrent.duration.FiniteDuration

/**
 * A object that con be used to create a [[Observable]] that emits a value every time a given time interval has passed.
 */
object TimeFlow:
  /**
   * Creates a [[Observable]] that emits a value every time a given time interval has passed.
   *
   * @param duration the time interval between two consecutive values
   * @return an [[Observable]] that emits a value every time a given time interval has passed
   */
  def tickEach(duration: FiniteDuration): Observable[Long] =
    Observable
      .fromIterable(LazyList.continually(duration))
      .delayOnNext(duration)
      .map(_.toMillis)
