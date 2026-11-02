fun showMenu(title: String, items: List<String>): Int? {
    while (true) {
        println()
        println(title)
        items.forEachIndexed { index, item ->
            println("$index. $item")
        }
        println("Введите номер пункта:")
        val line = readlnOrNull() ?: return null
        val index = line.trim().toIntOrNull()
        if (index != null && index in items.indices) {
            return index
        }
        println("Введите число от 0 до ${items.lastIndex}")
    }
}

fun readNonBlank(prompt: String, emptyMessage: String): String? {
    var line: String?
    var value = ""
    do {
        println(prompt)
        line = readlnOrNull()
        value = line?.trim().orEmpty()
        if (line != null && value.isEmpty()) {
            println(emptyMessage)
        }
    } while (line != null && value.isEmpty())
    return if (line == null) null else value
}
