package view.gui

object PimpingByDouble:
  
  extension(n: Double)

    /**
    * Method that round a double value into two decimals
    * @return
    */
    def roundTwoDecimals =
        BigDecimal(n).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble