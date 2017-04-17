package markdown

enum class MarkdownElements(val regex: Regex, val replacement: String, val delimiter: String) {
    H1("""^#""".toRegex(RegexOption.MULTILINE), "%H1%", "%BREAK%"),
    H2("""^##""".toRegex(RegexOption.MULTILINE), "%H2%", "%BREAK%"),
    UL("""^\*\W""".toRegex(RegexOption.MULTILINE), "%UL%", "%BREAK%"),
    ITALIC("""\*\*""".toRegex(), "%IT%", "%IT%"),
    BOLD("""\*""".toRegex(), "%B%", "%B%"),
    QUOTE("""^>""".toRegex(RegexOption.MULTILINE), "%QT%", "%BREAK%"),
    BREAK("""\n|\r|$""".toRegex(), "%BREAK%", "");

    companion object {
        fun toList(): List<MarkdownElements> = listOf(H2, H1, UL, ITALIC, BOLD, QUOTE, BREAK)
    }
}
