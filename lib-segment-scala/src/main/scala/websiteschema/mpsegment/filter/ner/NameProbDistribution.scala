package websiteschema.mpsegment.filter.ner

import websiteschema.mpsegment.util.{FileUtil, SerializeHandler}
import websiteschema.mpsegment.pinyin.WordToPinyinClassfierFactory

class NameProbDistribution {

  val loader = SerializeHandler(FileUtil.getResourceAsStream("ner_cn.dat"))
  val wordCount = loader.deserializeInt()
  val pinyinFreq = loader.deserializeMapStringInt()
  val wordFreq = loader.deserializeMapStringInt()
  val xingSet = loader.deserializeArrayString().toSet

  def getProbAsName(words: List[String]): Double = {
    val prob = (words.map(word => if (wordFreq.containsKey(word)) wordFreq.get(word) else 1).sum.toDouble) / wordCount.toDouble
    Math.log(prob) / Math.log(2)
  }

  def getPinyinProbAsName(words: List[String]): Double = {
    val pinyinList = WordToPinyinClassfierFactory().getClassifier().classify(words.mkString)
    val prob = (pinyinList.map(pinyin => if (pinyinFreq.containsKey(pinyin)) pinyinFreq.get(pinyin) else pinyinFreq.get("unknown")).sum.toDouble) / wordCount.toDouble
    Math.log(prob) / Math.log(2)
  }
}
