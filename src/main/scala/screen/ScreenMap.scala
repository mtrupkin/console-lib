package me.mtrupkin.tile

import java.io._
import java.nio.{ByteOrder, ByteBuffer}
import java.nio.file._
import java.util.zip.{GZIPOutputStream, GZIPInputStream}

import console.screen.{RGBColor, ScreenChar}
import org.apache.commons.io.IOUtils

// Created on 11/21/2014.

case class ScreenMap(name: String, width: Int, height: Int, matrix: Seq[Seq[ScreenChar]])

object ScreenMap {

    val EXTENDED: Seq[Char]  = Array( 0x00C7, 0x00FC, 0x00E9, 0x00E2,
      0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF,
      0x00EE, 0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4,
      0x00F6, 0x00F2, 0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2,
      0x00A3, 0x00A5, 0x20A7, 0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA,
      0x00F1, 0x00D1, 0x00AA, 0x00BA, 0x00BF, 0x2310, 0x00AC, 0x00BD,
      0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591, 0x2592, 0x2593, 0x2502,
      0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
      0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C, 0x251C,
      0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
      0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
      0x2558, 0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588,
      0x2584, 0x258C, 0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0,
      0x03A3, 0x03C3, 0x00B5, 0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4,
      0x221E, 0x03C6, 0x03B5, 0x2229, 0x2261, 0x00B1, 0x2265, 0x2264,
      0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0, 0x2219, 0x00B7, 0x221A,
      0x207F, 0x00B2, 0x25A0, 0x00A0 ).toSeq.map(_.toChar)

    def getAscii(code: Int): Char = {
      if (code >= 0x80 && code <= 0xFF) {
        EXTENDED(code - 0x80)
      } else { code.toChar }
    }

  // change from list of rows to list of columns
  protected def changeAxis(m: Seq[Seq[ScreenChar]], acc: Seq[Seq[ScreenChar]]): Seq[Seq[ScreenChar]] = {
    if (!m.head.isEmpty) {
      val row = for {
        first <- m
      } yield first.head

      val remainder = for {
        first <- m
      } yield first.tail

      changeAxis(remainder, acc :+ row)
    } else {
      acc
    }
  }

  def readXP(filename: String): ScreenMap = {
    class LittleEndianDataInputStream(i: InputStream) extends DataInputStream(i) {
      def readLongLE(): Long = java.lang.Long.reverseBytes(super.readLong())
      def readIntLE(): Int = java.lang.Integer.reverseBytes(super.readInt())
      def readCharLE(): Char = java.lang.Character.reverseBytes(super.readChar())
      def readShortLE(): Short = java.lang.Short.reverseBytes(super.readShort())
    }

    def readMatrix(is: LittleEndianDataInputStream, width: Int, height: Int): Seq[Seq[ScreenChar]] = {
      val m = for {
        i <- 0 until width
      } yield for {
          j <- 0 until height
        } yield readScreenChar(is)

      //changeAxis(m, Nil)
      m
    }
    def readScreenChar(is: LittleEndianDataInputStream): ScreenChar = ScreenChar(getAscii(is.readIntLE().toChar), readRGBColor(is), readRGBColor(is))
    def readRGBColor(is: DataInputStream): RGBColor = RGBColor(is.readByte() & 0xff, is.readByte() & 0xff, is.readByte() & 0xff)

    val path = Paths.get(filename)
    val gzipInput = new GZIPInputStream(Files.newInputStream(path))
    val dataInput = new LittleEndianDataInputStream(gzipInput)

    val version = dataInput.readIntLE()
    val layers = dataInput.readIntLE()
    val width = dataInput.readIntLE()
    val height = dataInput.readIntLE()

    val matrix = readMatrix(dataInput, width, height)

    gzipInput.close()

    ScreenMap(path.getFileName().toString, width, height, matrix)
  }


  def writeXP(path: String, screenMap: ScreenMap): Unit = {
    class LittleEndianDataOutputStream(o: OutputStream) extends DataOutputStream(o) {
      def writeIntLE(v: Int): Unit = {
        val buffer = ByteBuffer.allocate(4)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(v)
        super.write(buffer.array())
      }
    }
    def writeMatrix(matrix: Seq[Seq[ScreenChar]], os: LittleEndianDataOutputStream): Unit = {
      val m = changeAxis(matrix, Nil)

      for (row <- m) {
        for (cell <- row) {
          writeScreenChar(cell, os)
        }
      }
    }

    def writeScreenChar(sc: ScreenChar, os: LittleEndianDataOutputStream): Unit = {
      os.writeIntLE(sc.c.toInt)
      writeRGBColor(sc.fg, os)
      writeRGBColor(sc.bg, os)
    }

    def writeRGBColor(c: RGBColor, os: DataOutputStream): Unit = {
      os.writeByte(c.r)
      os.writeByte(c.g)
      os.writeByte(c.b)
    }

    val fos = Files.newOutputStream(Paths.get(path), StandardOpenOption.CREATE)
    val gzipOutput = new GZIPOutputStream(fos)

    val dataOutput = new LittleEndianDataOutputStream(gzipOutput)

    dataOutput.writeIntLE(-1)
    dataOutput.writeIntLE(1)

    dataOutput.writeIntLE(screenMap.width)
    dataOutput.writeIntLE(screenMap.height)

    writeMatrix(screenMap.matrix, dataOutput)

    gzipOutput.close()
  }


  def writeConsole(path: String, screenMap: ScreenMap): Unit = {
    val writer = Files.newBufferedWriter(Paths.get(path), StandardOpenOption.CREATE)

    writer.write("! CONSOLE v0.0.1")
    writer.newLine()
    writer.write(s"${screenMap.width} ${screenMap.height}")
    writer.newLine()

    for (row <- screenMap.matrix) {
      for (cell <- row) {
        writer.write(cell.c)
      }
      writer.newLine()
    }

    writer.close()
  }

  def readConsole(path: String): ScreenMap = {
    val reader = Files.newBufferedReader(Paths.get(path))
    reader.readLine() // ignore first line
    val s = reader.readLine().split(" ")
    val width = Integer.parseInt(s(0))
    val height = Integer.parseInt(s(1))

    var line: Option[String] = None

    var matrix: Seq[Seq[ScreenChar]] = Nil

    do {
      line = Option(reader.readLine())

      for {
        s <- line
      } yield {
        matrix = matrix :+ s.map(c => ScreenChar(c, RGBColor.White, RGBColor.Black))
      }

    } while (line != None)

    ScreenMap("test2", width, height, matrix)
  }
}