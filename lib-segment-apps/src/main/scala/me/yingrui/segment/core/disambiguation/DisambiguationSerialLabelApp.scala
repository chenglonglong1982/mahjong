package me.yingrui.segment.core.disambiguation

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}

import me.yingrui.segment.core.SegmentWorker
import me.yingrui.segment.tools.PFRCorpusLoader
import me.yingrui.segment.tools.accurary.SegmentResultComparator
import me.yingrui.segment.util.FileUtil._

object DisambiguationSerialLabelApp extends App {
  val resource = "./lib-segment/src/test/resources/PFR-199801-utf-8.txt"
  val writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("disambiguation-corpus.txt"), "utf-8"))
  val loader = PFRCorpusLoader(getResourceAsStream(resource))

  val segmenter = SegmentWorker("separate.xingming" -> "true")
  loader.load(expect => {
    val originalString = expect.toOriginalString()
    val actual = segmenter.segment(originalString)

    val hooker = new DisambiguationToSerialLabels(expect, actual)
    val comparator = new SegmentResultComparator(hooker)
    comparator.compare(expect, actual)

    val labels = hooker.serialLabels
    labels.foreach(t => writer.println(t._1 + " " + t._2))
    writer.println()
  })
}
