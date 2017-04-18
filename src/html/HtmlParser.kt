package html

import markdown.MarkdownElements
import markdown.MarkdownToken


class HtmlParser {

    /**
     * For all tokens in the list that need to be symetric as returned by [MarkdownElements.getSymetricSymbols]
     * (ie. have an opening and closing tag), check if the total number of tokens of that type is even.
     * If not drop the last token of that type to ensure symetric tags are always closed.
     *
     * @return A list of tokens that is guaranteed to have an even number of symetric tokens
     */
    fun dropDanglingSymetricTags(tokens: List<MarkdownToken>): List<MarkdownToken> {
        return tokens.minus(findDanglingTags(tokens))
    }

    private fun findDanglingTags(collection: List<MarkdownToken>): List<MarkdownToken> {
        return MarkdownElements.getSymetricSymbols()
                .map { elementType -> getDanglingTag(collection, elementType) }
                .filterNotNull()
    }

    private fun getDanglingTag(collection: List<MarkdownToken>, elementType: MarkdownElements): MarkdownToken? {
        return collection.findLast { hasOddCount(collection, elementType) && it.element == elementType }
    }

    private fun hasOddCount(collection: List<MarkdownToken>, elementType: MarkdownElements): Boolean {
        return collection.filter { it.element == elementType }.size % 2 != 0
    }
}