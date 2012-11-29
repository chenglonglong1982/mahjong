package websiteschema.mpsegment;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.dict.POSUtil;

public class MPSegmentTest {

    @Test
    public void should_Know_How_to_Break_ChinaGreatWall() {
        String str = "中国长城";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.length(), 2);
        Assert.assertEquals(words.getWord(0), "中国");
        Assert.assertEquals(words.getPOS(0), POSUtil.POS_NS);
        Assert.assertEquals(words.getWord(1), "长城");
        Assert.assertEquals(words.getPOS(1), POSUtil.POS_NS);
    }

    @Test
    public void should_Know_Some_Chinese_Names() {
        String str = "张三丰创造了太极拳。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "张三丰");
        Assert.assertEquals(words.getPOS(0), POSUtil.POS_NR);
        Assert.assertEquals(words.getPOS(1), POSUtil.POS_V);
        Assert.assertEquals(words.getPOS(2), POSUtil.POS_U);
        Assert.assertEquals(words.getPOS(3), POSUtil.POS_N);
        Assert.assertEquals(words.getPOS(4), POSUtil.POS_W);

        str = "太极拳的创始人是张三丰";
        words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(4), "张三丰");
    }

    @Test
    public void should_seperate_Chinese_Name_into_xing_and_ming() {
        String str = "张三丰创造了太极拳。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker("segment.xingmingseparate -> true");
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals("张", words.getWord(0));
        Assert.assertEquals("三丰", words.getWord(1));
        Assert.assertEquals(POSUtil.POS_NR, words.getPOS(0));
        Assert.assertEquals(POSUtil.POS_NR,words.getPOS(1));
    }

    @Test
    public void should_Support_Query_Syntax() {
        String str = "中国~[250]";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker("segment.querysyntax -> true");
        SegmentResult words = worker.segment(str);
        System.out.print(words + " ");
        Assert.assertEquals(words.getWord(0), "中国~[250]");

        str = "中国*";
        words = worker.segment(str);
        System.out.print(words + " ");
        Assert.assertEquals(words.getWord(0), "中国*");

        str = "中国:title社会";
        words = worker.segment(str);
        System.out.print(words + " ");
        Assert.assertEquals(words.getWord(0), "中国:TITLE");

        str = "中国?";
        words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "中国?");
    }

    @Test
    public void should_Recognize_Some_Chinese_Place_Names() {
        String str = "7月去了德江县。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals("德江县", words.getWord(3));
    }

    @Test
    public void should_Recognize_Date_and_Time() {
        String str = "7月1日10时计划开始。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals("7月", words.getWord(0));
        Assert.assertEquals("1日", words.getWord(1));
        Assert.assertEquals("10时", words.getWord(2));
        Assert.assertEquals(POSUtil.POS_T, words.getPOS(0));
        Assert.assertEquals(POSUtil.POS_T, words.getPOS(1));
        Assert.assertEquals(POSUtil.POS_T, words.getPOS(2));
    }

    @Test
    public void should_Know_How_to_Do_UpperCase_And_HalfShape() {
        String str = "Ａ计划和b计划";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "A");
        Assert.assertEquals(words.getWord(3), "B");
    }

    @Test
    public void should_Know_How_to_Handle_English_Words() {
        String str = "张三丰created太极拳。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(1), "CREATED");
        Assert.assertEquals(words.getPOS(1), POSUtil.POS_UNKOWN);
    }

    @Test
    public void should_Know_How_to_Handle_Date() {
        String str = "中华人民共和国在1949年10月1日正式宣布成立。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(2), "1949年");
        Assert.assertEquals(words.getPOS(2), POSUtil.POS_T);
        Assert.assertEquals(words.getWord(3), "10月");
        Assert.assertEquals(words.getPOS(3), POSUtil.POS_T);
        Assert.assertEquals(words.getWord(4), "1日");
        Assert.assertEquals(words.getPOS(4), POSUtil.POS_T);
    }

    @Test
    public void should_segment_big_word_to_litter_words() {
        String str = "中华人民共和国在1949年10月1日正式宣布成立。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker("segment.min -> true");
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "中华");
        Assert.assertEquals(words.getWord(1), "人民");
        Assert.assertEquals(words.getWord(2), "共和国");
    }

    @Test
    public void should_segment_big_word_to_litter_words_except_POS_I_L() {
        String str = "习惯成自然是一句俗语。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker("segment.min = true");
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "习惯成自然");
        Assert.assertEquals(words.getPOS(0), POSUtil.POS_I);
    }

    @Test
    public void should_return_concept_info_when_segment() {
        String str = "麦片是一种食物。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getConcept(0), "n-food");
        Assert.assertEquals(words.getConcept(1), "N/A");
        Assert.assertEquals(words.getConcept(2), "N/A");
        Assert.assertEquals(words.getConcept(3), "N/A");
        Assert.assertEquals(words.getConcept(4), "N/A");
    }

    @Test
    public void should_return_pinyin_when_segment() {
        String str = "中文分词。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker("segment.pinyin = true");
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getPinyin(0), "zhong'wen");
        Assert.assertEquals(words.getPinyin(1), "fen'ci");
        Assert.assertEquals(words.getPinyin(2), "。");
    }

    @Test
    public void should_know_stop_vertex_in_multi_sections_situation() {
        String str =
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111," +
                "中华人民共和国在1949年10月1日正式宣布成立，从此中国人民走上了繁荣富强的正确道路。";
        System.out.println(str.length());
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();
        SegmentResult words = worker.segment(str);
        boolean containsPRC = false;
        for (int i = 0; i < words.length(); i++) {
            if (words.getWord(i).equals("中华人民共和国")) {
                containsPRC = true;
            }
        }
        assert (containsPRC);
    }
}
