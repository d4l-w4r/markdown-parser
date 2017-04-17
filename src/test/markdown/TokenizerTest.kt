package test.markdown

import junit.framework.TestCase.assertEquals
import markdown.MarkdownElements
import markdown.Tokenizer
import org.junit.Test

class TokenizerTest {


    @Test
    fun single_hashtag_is_ignored_if_not_at_start_of_line() {
        // given
        val testString = "This is not a #Header Test."
        val expectedString = "This is not a #Header Test.%BREAK%"
        val expectedNumberOfTokens = 1
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
    }

    @Test
    fun single_hashtag_is_tokenized_to_h1_header_if_its_at_start_of_line() {
        // given
        val testString = "#Header Test.\nAnd this is a new line"
        val expectedString = "%H1%Header Test.%BREAK%And this is a new line%BREAK%"
        val expectedNumberOfTokens = 3
        val expectedTokenTypes = listOf(MarkdownElements.H1, MarkdownElements.BREAK, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(0, 16, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }

    @Test
    fun double_hashtags_are_tokenized_to_h2_header_if_at_start_of_line() {
        // given
        val testString = "##Subheader...and it's\ngone!"
        val expectedString = "%H2%Subheader...and it's%BREAK%gone!%BREAK%"
        val expectedNumberOfTokens = 3
        val expectedTokenTypes = listOf(MarkdownElements.H2, MarkdownElements.BREAK, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(0, 24, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }

    @Test
    fun double_hashtags_are_ignored_if_not_at_start_of_line() {
        // given
        val testString = "This is not a ##Subheader."
        val expectedString = "This is not a ##Subheader.%BREAK%"
        val expectedNumberOfTokens = 1
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
    }


    @Test
    fun single_asterix_is_tokenized_to_bold() {
        // given
        val testString = "And this my dear friend, is *bold*..."
        val expectedString = "And this my dear friend, is %B%bold%B%...%BREAK%"
        val expectedNumberOfTokens = 3
        val expectedTokenTypes = listOf(MarkdownElements.BOLD, MarkdownElements.BOLD, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(28, 35, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }

    @Test
    fun double_asterix_is_tokenized_to_italic() {
        // given
        val testString = "And this is cursive **italics** ..."
        val expectedString = "And this is cursive %IT%italics%IT% ...%BREAK%"
        val expectedNumberOfTokens = 3
        val expectedTokenTypes = listOf(MarkdownElements.ITALIC, MarkdownElements.ITALIC, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(20, 31, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }

    @Test
    fun multi_word_italized_text_can_be_tokenized() {
        // given
        val testString = "And this is cursive **text and if everything is good then this as well** ..."
        val expectedString = "And this is cursive %IT%text and if everything is good then this as well%IT% ...%BREAK%"
        val expectedNumberOfTokens = 3
        val expectedTokenTypes = listOf(MarkdownElements.ITALIC, MarkdownElements.ITALIC, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(20, 72, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }

    @Test
    fun nested_asterix_is_tokenized_to_bold_in_an_italic_string() {
        // given
        val testString = "And this is cursive **text and if *everything is good* then this as well** ..."
        val expectedString = "And this is cursive %IT%text and if %B%everything is good%B% then this as well%IT% ...%BREAK%"
        val expectedNumberOfTokens = 5
        val expectedTokenTypes = listOf(MarkdownElements.ITALIC, MarkdownElements.BOLD, MarkdownElements.BOLD, MarkdownElements.ITALIC, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(20, 36, 57, 78, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }

    @Test
    fun lt_sign_is_ignored_if_not_at_start_of_line() {
        // given
        val testString = "This is > not a quote."
        val expectedString = "This is > not a quote.%BREAK%"
        val expectedNumberOfTokens = 1
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
    }

    @Test
    fun lt_sign_is_tokenized_to_quote_if_its_at_start_of_line() {
        // given
        val testString = ">Quote test.\nAnd this is a new line"
        val expectedString = "%QT%Quote test.%BREAK%And this is a new line%BREAK%"
        val expectedNumberOfTokens = 3
        val expectedTokenTypes = listOf(MarkdownElements.QUOTE, MarkdownElements.BREAK, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(0, 15, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }

    @Test
    fun lt_sign_is_tokenized_to_quote_if_its_at_start_of_line_and_whitespace_before_word() {
        // given
        val testString = "> Quote test.\nAnd this is a new line"
        val expectedString = "%QT% Quote test.%BREAK%And this is a new line%BREAK%"
        val expectedNumberOfTokens = 3
        val expectedTokenTypes = listOf(MarkdownElements.QUOTE, MarkdownElements.BREAK, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(0, 16, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()

        // when
        val result = testee.tokenize(testString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }


    @Test
    fun highlight_all_tokens_in_a_string() {
        // given
        val inputString = "#A test of how well tokenization works\n\n##We try this with a limited subset for now\nThings that should work are: #h1 headers, ##h2 headers, *bold modifiers*, and **italic modifiers**"
        val expectedString = "%H1%A test of how well tokenization works%BREAK%%BREAK%%H2%We try this with a limited subset for now%BREAK%Things that should work are: #h1 headers, ##h2 headers, %B%bold modifiers%B%, and %IT%italic modifiers%IT%%BREAK%"
        val expectedNumberOfTokens = 10
        val expectedTokenTypes = listOf(MarkdownElements.H1, MarkdownElements.BREAK, MarkdownElements.BREAK, MarkdownElements.H2, MarkdownElements.BREAK, MarkdownElements.BOLD, MarkdownElements.BOLD, MarkdownElements.ITALIC, MarkdownElements.ITALIC, MarkdownElements.BREAK)
        val expectedTokenIndices = listOf(0, 41, 48, 55, 100, 163, 180, 189, 209, 213, expectedString.length - MarkdownElements.BREAK.replacement.length)
        val testee = Tokenizer()


        // when
        val result = testee.tokenize(inputString)
        val resultString = result.first
        val resultTokens = result.second

        // then
        assertEquals(expectedNumberOfTokens, resultTokens.size)
        assertEquals(expectedString, resultString)
        resultTokens.forEachIndexed {
            index, token ->
            assertEquals(expectedTokenTypes.get(index), token.element)
            assertEquals(expectedTokenIndices.get(index), token.startIndex)
        }
    }
}

