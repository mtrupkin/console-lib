import java.io.{SequenceInputStream, BufferedInputStream, FileInputStream}
import java.nio
import java.nio.file.Files

import sbt._
import org.apache.commons.compress.archivers.sevenz._

object CompressBuild extends Build {

	val compress = TaskKey[Unit]("compress", "Compress artifact")
	val compressDirectory = SettingKey[File]("Compress source directory")
	//val compressTarget = SettingKey[File]("Compress destination  file")

	override lazy val settings = super.settings ++
	    Seq(
				compress := runCompress.value
	    )

	private def runCompress: Def.Initialize[Task[File]] = Def.task {
		println("runCompress")

		val sfx =  new File(Path.userHome.absolutePath + "/Desktop/console-lib/src/build/windows/", "7z.sfx")
		val compressDirectory = new File(Path.userHome.absolutePath + "/Desktop/console-lib/target/launcher/build")
		val compressTarget =  new File(Path.userHome.absolutePath + "/Desktop/console-lib/target/", "test.7z")
		val sfxTarget =  new File(Path.userHome.absolutePath + "/Desktop/console-lib/target/", "test.exe")

		// compress distribution
		val compressFiles = for {
			file <- compressDirectory.**(new SimpleFileFilter(f=>f.isFile)).get
			name <- file.relativeTo(compressDirectory)
		} yield file -> (new File("test") / name.toString).toString


		val sevenZOutput = new SevenZOutputFile(compressTarget)
		sevenZOutput.setContentCompression(SevenZMethod.LZMA2)
		for {
			e <- compressFiles
		} yield {
			val entry = sevenZOutput.createArchiveEntry(e._1, e._2)
			sevenZOutput.putArchiveEntry(entry)
			writeEntry(sevenZOutput, e._1)
			sevenZOutput.closeArchiveEntry()
		}
		sevenZOutput.close()

		val sequence = new SequenceInputStream(new BufferedInputStream(new FileInputStream(sfx)), new BufferedInputStream(new FileInputStream(compressTarget)))
		Files.copy(sequence, sfxTarget.toPath)


		println("runCompress - completed")
		compressTarget
	}

	def writeEntry(sz: SevenZOutputFile, f: File): Unit = {
		val is = new BufferedInputStream(new FileInputStream(f))

		var byte = 0
		while (byte >= 0) {
			byte = is.read
			if (byte >= 0) sz.write(byte)
		}

		is.close()

	}
}
