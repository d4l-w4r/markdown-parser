package markdown

class MarkdownParser {

    /**
     * For each supported markdown element specified in [MarkdownElements]
     * replace each occurence of the element in the input string with the corresponding replacement token
     * and store all tokens and their start index in a list.
     *
     * @return A [Pair] containing the transformed string and a list of matched tokens
     */
    fun tokenize(rawString: String): Pair<String, List<MarkdownToken>> {
        val tokenizedString = replaceWithTokens(rawString)
        val tokens = MarkdownElements.toList()
                .flatMap { element -> findMatches(element, tokenizedString).toList() }
                .sortedBy { token -> token.startIndex }

        return Pair(tokenizedString, tokens)
    }

    private fun replaceWithTokens(rawString: String): String {
        var tokenizedString = rawString
        MarkdownElements.toList()
                .forEach {
                    element -> tokenizedString = element.regex.replace(tokenizedString, element.replacement)
                }
        return tokenizedString
    }

    private fun findMatches(element: MarkdownElements, string: String): Sequence<MarkdownToken> {
        return element.replacement.toRegex()
                .findAll(string)
                .map { match -> MarkdownToken(element, match.range.first) }
    }
}
