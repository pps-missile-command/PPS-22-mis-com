package model.missile.zigzag

object PimpingByDouble:

  extension(n: Double)

    /**
    * Extension method to model a map function that given a double number returns 1 or -1 whether the number is
    * greater than a threshold or not
    * @return the number positive in case of threshold violation and negative otherwise
    */
    def mapToSign = n match
      case v if v > signThreshold => 1
      case _ => -1