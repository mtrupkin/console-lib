package me.mtrupkin.console.screen

import play.api.libs.json._

/**
 * User: mtrupkin
 * Date: 7/5/13
 */
case class RGBColor(r: Int, g: Int, b:Int) {
  override def toString(): String = {
    f"#${r}%02X$g%02X$b%02X"
  }
}

object RGBColor {
  implicit object RGBColorFormat extends Format[RGBColor] {
    def reads(json: JsValue): JsResult[RGBColor] = JsSuccess(RGBColor(json.as[String]))
    def writes(u: RGBColor): JsValue = JsString(u.toString)
  }

  def apply(s: String): RGBColor = {
    def next(s0: String): String = {
      s0.substring(0, 2)
    }

    def toInt(h: String): Int = {
      Integer.parseInt(h, 16)
    }

    val r = next(s.substring(1))
    val g = next(s.substring(3))
    val b = next(s.substring(5))

    RGBColor(toInt(r), toInt(g), toInt(b))
  }
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
