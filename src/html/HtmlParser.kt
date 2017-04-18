package html

import markdown.MarkdownElements
import markdown.Token


class HtmlParser {

    /**
     * For all tokens in the list that need to be symetric as returned by [MarkdownElements.getSymetricSymbols]
     * (ie. have an opening and closing tag), check if the total number of tokens of that type is even.
     * If not drop the last token of that type to ensure symetric tags are always closed.
     *
     * @return A list of tokens that is guaranteed to have an even number of symetric tokens
     */
    fun dropDanglingSymetricTags(tokens: List<Token>): List<Token> {
        return tokens.minus(findDanglingTags(tokens))
    }

    private fun findDanglingTags(collection: List<Token>): List<Token> {
        return MarkdownElements.getSymetricSymbols()
                .map { elementType -> getDanglingTag(collection, elementType) }
                .filterNotNull()
    }

    private fun getDanglingTag(collection: List<Token>, elementType: MarkdownElements): Token? {
        return collection.findLast { hasOddCount(collection, elementType) && it.element == elementType }
    }

    private fun hasOddCount(collection: List<Token>, elementType: MarkdownElements): Boolean {
        return collection.filter { it.element == elementType }.size % 2 != 0
    }
}