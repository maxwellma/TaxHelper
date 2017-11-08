package com.maxwell.mclib.util

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination

/**
 * Created by maxwellma on 04/11/2017.
 */
object PinyinUtil {

    fun getPinyin(string: String): String {
        val format = HanyuPinyinOutputFormat()
        // 不需要音标
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        // 设置转换出大写字母
        format.caseType = HanyuPinyinCaseType.UPPERCASE

        val charArray = string.toCharArray()

        val sb = StringBuilder()

        charArray.indices
                .map {
                    charArray[it]

                    // 如果是空格, 跳过当前循环
                }
                .filterNot { Character.isWhitespace(it) }
                .forEach {
                    if (it.toInt() >= -128 && it.toInt() < 127) {
                        // 不可能是汉字, 直接拼接
                        sb.append(it)
                    } else {
                        try {
                            // 获取某个字符对应的拼音. 可以获取到多音字(多音字只选择第一个拼音). 单 ->DAN, SHAN
                            val s = PinyinHelper.toHanyuPinyinStringArray(it, format)
                            sb.append(s[0])
                        } catch (e: BadHanyuPinyinOutputFormatCombination) {
                            e.printStackTrace()
                        }

                    }
                }

        return sb.toString()
    }

}