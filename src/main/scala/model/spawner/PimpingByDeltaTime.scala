package model.spawner

import model.DeltaTime

object PimpingByDeltaTime:
  /**
   * Extension methods for DeltaTime
   */
  extension(interval: DeltaTime)

  /**
   * The hyperbole function that given a timePassed delta time return the updated interval decreased
   * by the actual f(timePassed) value
   */
    def ~(timePassed: DeltaTime) : DeltaTime =
      interval - ((interval * (1 / ( 1 + Math.exp(((-1 * timePassed) / 10))))) - (interval / 2))
      
    /**
     * Function that given a condition and a world può inficiare positivamente sulle lezaione.
     * @param condition The condition to which we can subscreibe
     * @param mapper
     * @return
     */
    def mapIf(condition: () => Boolean)(mapper: (DeltaTime) => DeltaTime): DeltaTime = condition match
      case _ if condition() => mapper(interval)
      case _ => interval