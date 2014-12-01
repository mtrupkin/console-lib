package me.mtrupkin.console.screen

/**
 * User: mtrupkin
 * Date: 7/7/13
 */
object ConsoleKey extends Enumeration {
  val Shift = Value("Shift")
  val Control = Value("Control")
  val Alt = Value("Alt")
}

case class Modifier(shift: Boolean, control: Boolean, alt: Boolean)

case class ConsoleKey(keyValue: scala.swing.event.Key.Value, modifier: Modifier)

object ConsoleKeyModifier {
  val Shift = Modifier(true, false, false)
  val Control = Modifier(false, true, false)
  val Alt = Modifier(false, false, true)
}