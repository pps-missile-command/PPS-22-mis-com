package model.elements2d

enum Angle:
  case Degree(value: Double)
  case Radian(value: Double)

  def radiant: Double = this match
    case Degree(value) if value % 360 > 180 => (-180 + value % 180) * Math.PI / 180
    case Degree(value) => value * Math.PI / 180
    case Radian(value) if (value % (2 * Math.PI)) > Math.PI => -Math.PI + value % Math.PI
    case Radian(value) => value

  def degree: Double = this match
    case Degree(value) if value % 360 > 180 => -180 + value % 180
    case Degree(value) => value
    case Radian(value) if (value % (2 * Math.PI)) > Math.PI => -180 + ((value * 180 / Math.PI) % 180)
    case Radian(value) => value * 180 / Math.PI
