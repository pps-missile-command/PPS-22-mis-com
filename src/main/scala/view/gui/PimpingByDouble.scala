package view.gui

object PimpingByDouble:
  
  extension(n: Double)
    def roundTwoDecimals =
      BigDecimal(n).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble