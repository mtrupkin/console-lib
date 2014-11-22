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
  val inputFilename = "C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test2.xp"
  val uncOutputFilename = "C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test2.unc.xp"
  val outputFilename = "C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test3.xp"

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
    for {
      i <- 0 until height
    } yield for {
      j <- 0 until width
    } yield readScreenChar(is)
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
    for (row <- matrix) {
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

  def saveXML(screenMap: ScreenMap): Unit = {
    val marshalled =
<image>
  <name>{screenMap.name}</name>
  <width>{screenMap.width}</width>
  <height>{screenMap.height}</height>
  <data>
    {for (row <- screenMap.matrix) yield
    <row>
      {for (cell <- row) yield
        <cell><ascii>{cell.c.toInt}</ascii><fgd>{cell.fg.toHexString}</fgd><bkg>{cell.bg.toHexString}</bkg></cell>
      }
    </row>}
  </data>
</image>

    scala.xml.XML.save("C:\\Users\\mtrupkin\\Downloads\\REXPaint_v0.99r9\\REXPaint-R9\\images\\test2.xml", marshalled)
  }
}