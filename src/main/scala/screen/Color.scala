package console.screen

/**
 * User: mtrupkin
 * Date: 7/5/13
 */
case class RGBColor(r: Int, g: Int, b:Int)

object RGBColor {
  val Black = RGBColor(0, 0, 0)
  val White = RGBColor(255, 255, 255)
  val LightGrey = RGBColor(126, 126, 126)
  val Yellow = RGBColor(255, 255, 0)
  val Blue = RGBColor(0, 0, 255)
  val Red = RGBColor(255, 0, 0)
  val Green = RGBColor(0, 255, 0)
  val LightYellow = RGBColor(126, 126, 0)
  val LightBlue = RGBColor(21, 105, 199)
  val LightRed = RGBColor(126, 0, 0)
  val LightGreen = RGBColor(0, 126, 0)
}
