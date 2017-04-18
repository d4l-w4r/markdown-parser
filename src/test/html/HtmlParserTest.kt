package test.html

import html.HtmlParser
import markdown.MarkdownElements
import markdown.MarkdownToken
import org.junit.Assert.*
import org.junit.Test


class HtmlParserTest {

    @Test
    fun given_a_string_with_a_dangling_bold_tag_at_the_last_element_when_it_is_parsed_then_the_last_bold_tag_is_dropped() {
        // given
        val testTokens = listOf(MarkdownToken(MarkdownElements.BOLD, 0), MarkdownToken(MarkdownElements.BOLD, 10), MarkdownToken(MarkdownElements.BOLD, 15), MarkdownToken(MarkdownElements.BREAK, 16))
        val expectedResult = listOf(MarkdownToken(MarkdownElements.BOLD, 0), MarkdownToken(MarkdownElements.BOLD, 10), MarkdownToken(MarkdownElements.BREAK, 16))
        val testee = HtmlParser()

        // when
        val resultTokens = testee.dropDanglingSymetricTags(testTokens)

        // then
       resultTokens.forEachIndexed { index, token ->
           assertEquals(expectedResult[index].element, token.element)
           assertEquals(expectedResult[index].startIndex, token.startIndex)
       }
    }

    @Test
    fun given_a_string_with_a_dangling_bold_tag_surrounded_by_text_when_it_is_parsed_then_the_last_bold_tag_is_dropped() {
        // given
        val testTokens = listOf(MarkdownToken(MarkdownElements.BOLD, 0), MarkdownToken(MarkdownElements.BOLD, 10), MarkdownToken(MarkdownElements.BOLD, 15), MarkdownToken(MarkdownElements.BREAK, 25))
        val expectedResult = listOf(MarkdownToken(MarkdownElements.BOLD, 0), MarkdownToken(MarkdownElements.BOLD, 10), MarkdownToken(MarkdownElements.BREAK, 25))
        val testee = HtmlParser()

        // when
        val resultTokens = testee.dropDanglingSymetricTags(testTokens)

        // then
        resultTokens.forEachIndexed { index, token ->
            assertEquals(expectedResult[index].element, token.element)
            assertEquals(expectedResult[index].startIndex, token.startIndex)
        }
    }

    @Test
    fun given_a_string_without_dangling_bold_tags_when_it_is_parsed_then_the_token_list_is_unmodified() {
        // given
        val testTokens = listOf(MarkdownToken(MarkdownElements.BOLD, 0), MarkdownToken(MarkdownElements.BOLD, 10), MarkdownToken(MarkdownElements.BOLD, 15), MarkdownToken(MarkdownElements.BOLD, 20), MarkdownToken(MarkdownElements.BREAK, 25))
        val testee = HtmlParser()

        // when
        val resultTokens = testee.dropDanglingSymetricTags(testTokens)

        // then
        assertEquals(testTokens, resultTokens)
    }

}