package me.mtrupkin.console.layout

/**
 * User: mtrupkin
 * Date: 7/21/13
 */
object VPos {
  sealed trait EnumVal

  case object TOP extends EnumVal
  case object CENTER extends EnumVal
  case object BOTTOM extends EnumVal
}

object HPos {
  sealed trait EnumVal

  case object LEFT extends EnumVal
  case object CENTER extends EnumVal
  case object RIGHT extends EnumVal
}

case class Pos(vPos: Option[VPos.EnumVal], hPos: Option[HPos.EnumVal])

object Pos {
  val CENTER = Some(Pos(Some(VPos.CENTER), Some(HPos.CENTER)))
  val H_CENTER = Some(Pos(None, Some(HPos.CENTER)))
}

object VFill {
  sealed trait EnumVal

  case object TOP extends EnumVal
  case object BOTTOM extends EnumVal
}

object HFill {
  sealed trait EnumVal

  case object LEFT extends EnumVal
  case object RIGHT extends EnumVal
}

case class Fill(hFill: Option[HFill.EnumVal], vFill: Option[VFill.EnumVal])

object Fill {
  val FILL_RIGHT = Some(Fill(Some(HFill.RIGHT), None))
  val FILL_BOTTOM = Some(Fill(None, Some(VFill.BOTTOM)))
  val FILL = Some(Fill(Some(HFill.RIGHT), Some(VFill.BOTTOM)))
}

object Orientation {
  sealed trait EnumVal

  case object HORIZONTAL extends EnumVal
  case object VERTICAL extends EnumVal
}

case class Layout(fill: Option[Fill], pos: Option[Pos])

object Layout {
  val FILL_RIGHT = Some(Layout(Fill.FILL_RIGHT, None))
  val FILL = Some(Layout(Fill.FILL, None))
}