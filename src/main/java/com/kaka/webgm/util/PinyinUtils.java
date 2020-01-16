package com.kaka.webgm.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PinyinUtils {

    private static final Logger logger = LoggerFactory.getLogger(PinyinUtils.class);

    /**
     * 获得汉语拼音首字母
     *
     * @param chines 汉字
     * @return
     */
    public static String getAlpha(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char aNameChar : nameChar) {
            if (aNameChar > 128) {
                try {
                    if(PinyinHelper.toHanyuPinyinStringArray(aNameChar, defaultFormat).length > 0) {
                        pinyinName += PinyinHelper.toHanyuPinyinStringArray(aNameChar, defaultFormat)[0].charAt(0);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    logger.error(e.getMessage(),e);
                }
            } else {
                pinyinName += aNameChar;
            }
        }
        return pinyinName;
    }

    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     *
     * @param inputString 汉字
     * @return
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String output = "";
        if (inputString != null && inputString.length() > 0
                && !"null".equals(inputString)) {
            char[] input = inputString.trim().toCharArray();
            try {
                for (char anInput : input) {
                    if (Character.toString(anInput).matches(
                            "[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(anInput, format);
                        output += temp[0];
                    } else
                        output += Character.toString(anInput);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                logger.error("getPingYin error.", e);
            }
        } else {
            return "*";
        }
        return output;
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char aNameChar : nameChar) {
            if (aNameChar > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(aNameChar, defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    logger.error("converterToFirstSpell error.", e);
                }
            } else {
                pinyinName += aNameChar;
            }
        }
        return pinyinName;
    }
}
