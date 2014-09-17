package org.flagship.console.terminal


import java.awt.image.BufferStrategy

import scala.swing.{BorderPanel, Frame}
import scala.swing.event.{KeyReleased, Key, KeyPressed}
import java.awt.event.{MouseEvent, MouseAdapter, KeyEvent, KeyAdapter}
import org.flagship.console.{Size, Point}
import java.awt._
import org.flagship.console.Size
import org.flagship.console.screen._


/**
 * User: mtrupkin
 * Date: 7/5/13
 */
trait Terminal {
  val terminalSize: Size
  var closed = false
  //var keyQueue: List[ConsoleKey] = Nil
  var key: Option[ConsoleKey] = None
  var mouse: Point = Point.Origin

  def addMouseAdapter(mouseAdapter: MouseAdapter)
  def render(screen: Screen)
  def close()
}


class SwingTerminal(val terminalSize: Size = new Size(50, 20), windowTitle: String = "Swing Terminal") extends Frame with Terminal {
  val terminalCanvas = new TerminalCanvas(terminalSize)
  val normalTextFont = new Font("Consolas", Font.PLAIN, 18)
  val systemFont = new Font("Arial", Font.PLAIN, 10)

  peer.add(terminalCanvas)

  title = windowTitle
  ignoreRepaint = true
  visible = true
  resizable = false

  class TerminalKeyAdapter extends KeyAdapter {
    override def keyPressed(e: KeyEvent) {
      val modifiers = new ConsoleKeyModifier(e.isShiftDown, e.isAltDown, e.isControlDown)
      key = Some(new ConsoleKey( Key(e.getKeyCode),modifiers ))
    }

    override def keyReleased(e: KeyEvent) { key = None }
  }

  val keyListener = new TerminalKeyAdapter
  peer.addKeyListener(keyListener)
  terminalCanvas.addKeyListener(keyListener)

  val mouseAdapter = new MouseAdapter {
    override def mouseMoved(e: MouseEvent) {
      Option(e) match {
        case Some(x) => {
          val x: Int = e.getX / charSize.width
          val y: Int = e.getY / charSize.height
          mouse = new Point(x, y)
        }
        case _ => {}
      }
    }

    override def mouseClicked(e: MouseEvent) {
    }
  }

  terminalCanvas.addMouseListener(mouseAdapter)
  terminalCanvas.addMouseMotionListener(mouseAdapter)

  def addMouseAdapter(mouseAdapter: MouseAdapter) {
    terminalCanvas.addMouseListener(mouseAdapter)
    terminalCanvas.addMouseMotionListener(mouseAdapter)
  }

  override def closeOperation( ) {
    closed = true
    visible = false
    dispose()
  }

  override def close() {
    super.close()
    dispose()
  }

  pack()

//  val url = ClassLoader.getSystemResource("icon.png")
//  val img = Toolkit.getDefaultToolkit().createImage(url)
//  peer.setIconImage(img)

  peer.setLocationRelativeTo(null)

  terminalCanvas.createBufferStrategy(2)

  val charSize = terminalCanvas.charSize(terminalCanvas.getGraphics)
  //val charSize = Size(1, 1)


  def render(screen: Screen) {
    if (!closed) {
      terminalCanvas.render(screen)
    }
  }

  class TerminalCanvas(val terminalSize: Size) extends Canvas {

    def getGraphics2D(g: Graphics): Graphics2D = {
      g match {
        case g2: Graphics2D => {
          g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
          g2
        }
        case _ => throw new ClassCastException
      }
    }

    def getGraphics2D(buffer: BufferStrategy): Graphics2D = {
      getGraphics2D(buffer.getDrawGraphics)
    }

    override def getPreferredSize(): java.awt.Dimension = {
      val screenDim = screenSize(charSize(getGraphics()))
      new java.awt.Dimension(screenDim.width, screenDim.height)
    }

    def charSize(g: Graphics): Size = {
      val g2 = getGraphics2D(g)


      val fontMetrics = g.getFontMetrics(normalTextFont)
      //val bounds = fontMetrics.getStringBounds(" ", g)
      //val bounds = fontMetrics.getStringBounds(ACS.VLINE.toString, g)
      val width = fontMetrics.charWidth(ASCII.HLINE)

      val maxCharBounds = normalTextFont.getMaxCharBounds(g2.getFontRenderContext)
      val isAntiAliased = g2.getFontRenderContext.isAntiAliased


      //new Size(width , fontMetrics.getAscent()*2)
      //new Size(bounds.getWidth.toInt+1, bounds.getHeight.toInt+2)
      //new Size(width, fontMetrics.getHeight)
      Size(width, maxCharBounds.getBounds.height)
    }

    def screenSize(charSize: Size): Size = {
      new Size(terminalSize.width * charSize.width, terminalSize.height * charSize.height)
    }

    var lastPaintTime = System.currentTimeMillis()
    var frameCount = 0

    def render(screen: Screen) = {

      val bufferStrategy = getBufferStrategy
      val g2 = getGraphics2D(bufferStrategy)

      renderGraphics(screen, g2)

      if (!bufferStrategy.contentsLost()) {
        bufferStrategy.show()
      }

      g2.dispose()
    }

    def renderGraphics(screen: Screen, g2: Graphics2D) = {
      this.synchronized {
        g2.setFont(normalTextFont)

        val charDim = charSize(g2)
        val screenDim = screenSize(charDim)

        g2.setColor(java.awt.Color.BLACK)
        g2.fillRect(0, 0, screenDim.width, screenDim.height)

        g2.setColor(java.awt.Color.WHITE)


        screen.foreach(drawScreenCharacter)

        def drawScreenCharacter(p: Point, s: ScreenChar) {

          g2.setColor(toAwtColor(s.fg))
          drawString(p, s.c.toString)
        }

        def drawString(p: Point, s: String) {
          val p2 = toPixel(p)
          g2.drawString(s, p2.x, p2.y)
        }

        def toPixel(p: Point): Point = {
          Point(p.x * charDim.width, ((p.y+1) * charDim.height) - 4)
        }

        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastPaintTime
        val frameRate = (1000f/elapsedTime)
        lastPaintTime = currentTime
        frameCount = frameCount + 1

//        drawString(frameRate.toString, 1, 1)
//        drawString(frameCount.toString, 1, 2)

        g2.setFont(systemFont)
        val p1 = toPixel(Point(90, 35))
        //g2.drawString(frameRate.toString, p1.x, p1.y)
        val p2 = toPixel(Point(90, 36))
        //g2.drawString(frameCount.toString, p2.x, p2.y)

      }
    }
  }

  import org.flagship.console.screen.RGBColor._

  def toAwtColor(c: RGBColor): java.awt.Color = {
    c match {
      case Black => java.awt.Color.BLACK
      case White => java.awt.Color.WHITE
      case Yellow => java.awt.Color.YELLOW
      case Red => java.awt.Color.RED
      case Green => java.awt.Color.GREEN
      case Blue => java.awt.Color.BLUE
      case LightYellow => AwtColor.LightYellow
      case LightGreen => AwtColor.LightGreen
      case LightBlue => AwtColor.LightBlue
      case LightRed => AwtColor.LightRed
      case LightGrey => AwtColor.LightGrey
      case _ =>  ???
    }
  }
}

object AwtColor {
  val LightYellow = getAwtColor(RGBColor.LightYellow)
  val LightRed = getAwtColor(RGBColor.LightRed)
  val LightBlue = getAwtColor(RGBColor.LightBlue)
  val LightGreen = getAwtColor(RGBColor.LightGreen)
  val LightGrey = getAwtColor(RGBColor.LightGrey)

  def getAwtColor(c: org.flagship.console.screen.RGBColor): java.awt.Color = new java.awt.Color(c.r, c.g, c.b)
}



