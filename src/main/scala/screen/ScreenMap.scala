package me.trupkin.tile

import java.io._
import java.nio.{ByteOrder, ByteBuffer}
import java.nio.file._
import java.util.zip.{GZIPOutputStream, GZIPInputStream}

import console.screen.{RGBColor, ScreenChar}
import org.apache.commons.io.IOUtils

// Created on 11/21/2014.

case class ScreenMap(name: String, width: Int, height: Int, matrix: Seq[Seq[ScreenChar]]) {

}

object ScreenMap {
  val inputFilename = "C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test1a.xp"
  val uncOutputFilename = "C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test1a.unc.xp"
  val outputFilename = "C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test1a.out.xp"
  val outputXPMFilename = "C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test1a.xpm"

  class LittleEndianDataInputStream(i: InputStream) extends DataInputStream(i) {
    def readLongLE(): Long = java.lang.Long.reverseBytes(super.readLong())
    def readIntLE(): Int = java.lang.Integer.reverseBytes(super.readInt())
    def readCharLE(): Char = java.lang.Character.reverseBytes(super.readChar())
    def readShortLE(): Short = java.lang.Short.reverseBytes(super.readShort())
  }

  class LittleEndianDataOutputStream(o: OutputStream) extends DataOutputStream(o) {
    def writeIntLE(v: Int): Unit = {
      val buffer = ByteBuffer.allocate(4)
      buffer.order(ByteOrder.LITTLE_ENDIAN)
      buffer.putInt(v)
      super.write(buffer.array())
    }
  }

  def load(): ScreenMap = {
    val gzipInput = new GZIPInputStream(Files.newInputStream(Paths.get(inputFilename)))
    Files.copy(new GZIPInputStream(Files.newInputStream(Paths.get(inputFilename))), new File(uncOutputFilename).toPath, StandardCopyOption.REPLACE_EXISTING)

    val dataInput = new LittleEndianDataInputStream(gzipInput)

    val version = dataInput.readIntLE()
    val layers = dataInput.readIntLE()
    val width = dataInput.readIntLE()
    val height = dataInput.readIntLE()

    val matrix = readMatrix(dataInput, width, height)

    gzipInput.close()

    ScreenMap("test2", width, height, matrix)
  }

  def readMatrix(is: LittleEndianDataInputStream, width: Int, height: Int): Seq[Seq[ScreenChar]] = {
    val m = for {
      i <- 0 until width
    } yield for {
      j <- 0 until height
    } yield readScreenChar(is)

    changeAxis(m, Nil)
  }

  def readScreenChar(is: LittleEndianDataInputStream): ScreenChar = ScreenChar(is.readIntLE().toChar, readRGBColor(is), readRGBColor(is))

  def readRGBColor(is: DataInputStream): RGBColor = RGBColor(is.readByte(), is.readByte(), is.readByte())

  def save(screenMap: ScreenMap): Unit = {

    val fos = Files.newOutputStream(Paths.get(outputFilename), StandardOpenOption.CREATE)
    val gzipOutput = new GZIPOutputStream(fos)

    val dataOutput = new LittleEndianDataOutputStream(gzipOutput)

    dataOutput.writeIntLE(-1)
    dataOutput.writeIntLE(1)

    dataOutput.writeIntLE(screenMap.width)
    dataOutput.writeIntLE(screenMap.height)

    writeMatrix(screenMap.matrix, dataOutput)

    gzipOutput.close()
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

  def saveConsole(screenMap: ScreenMap): Unit = {
    val writer = Files.newBufferedWriter(Paths.get(outputXPMFilename), StandardOpenOption.CREATE)

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

  def loadConsole(): ScreenMap = {
    val reader = Files.newBufferedReader(Paths.get(outputXPMFilename))
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

  def changeAxis(m: Seq[Seq[ScreenChar]], acc: Seq[Seq[ScreenChar]]): Seq[Seq[ScreenChar]] = {
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
}