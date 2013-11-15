package websiteschema.mpsegment

import dict.POSUtil
import websiteschema.mpsegment.core.SegmentEngine
import org.junit.Test

class ExtendPOSInDomainDictionaryTest {

  @Test
  def should_set_extra_POS_to_domain_word() {
    SegmentEngine()

    try {
      val str = "我的同学叫高峰,高峰同志,高峰经理,科学高峰"
      val worker = SegmentEngine().getSegmentWorker()
      val words = worker.segment(str)
      for (i <- 0 until words.length) {
        println(words.getWord(i) + " - " + POSUtil.getPOSString(words.getPOS(i)) + " - " + words.getDomainType(i))
      }
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
    }
  }
}
