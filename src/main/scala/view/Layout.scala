package console.control

/**
 * User: mtrupkin
 * Date: 7/21/13
 */

case class LayoutOp(op: String)

case class LayoutFlow(flow: String)

object LayoutFlow extends Enumeration {
  val VERTICAL = Value("vertical")
  val HORIZONTAL = Value("horizontal")
}

object LayoutOp {
  val NONE: LayoutOp = LayoutOp("none")
  val SNAP: LayoutOp = LayoutOp("snap")
  val GRAB: LayoutOp = LayoutOp("grab")
}

case class Layout(
  left: LayoutOp = LayoutOp.NONE,
  right: LayoutOp = LayoutOp.NONE,
  top: LayoutOp = LayoutOp.NONE,
  bottom: LayoutOp = LayoutOp.NONE)

object Layout {
  val NONE: Layout = new Layout()
}
