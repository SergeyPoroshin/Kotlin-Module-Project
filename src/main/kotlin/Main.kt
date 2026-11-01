// Тут пришлось воспользоваться нейронкой, т.к сам запутался в причинах проблемы
// Вмето кириллицы были битые символы в консоли IDE, ниже инфа от нейронки и её решение
// Консоль Android Studio читает вывод как UTF-8, а Java 21 на Windows
// по умолчанию пишет в кодировке консоли (Cp866) — из-за этого кириллица ломается.
import java.io.PrintStream

data class Note(
    val name: String,
    val text: String,
)

data class Archive(
    val name: String,
    val notes: MutableList<Note> = mutableListOf(),
)

private sealed interface Screen {
    data object Archives : Screen
    data object CreateArchive : Screen
    data class OpenArchive(val archive: Archive) : Screen
    data class CreateNote(val archive: Archive) : Screen
    data class OpenNote(val note: Note) : Screen
    data class ViewNote(val note: Note) : Screen
}

fun main() {
    // Тут пришлось воспользоваться нейронкой, т.к сам запутался в причинах проблемы
    // Вмето кириллицы были битые символы в консоли IDE, ниже инфа от нейронки и её решение
    // Консоль Android Studio читает вывод как UTF-8, а Java 21 на Windows
    // по умолчанию пишет в кодировке консоли (Cp866) — из-за этого кириллица ломается.
    System.setOut(PrintStream(System.out, true, Charsets.UTF_8))
    System.setErr(PrintStream(System.err, true, Charsets.UTF_8))

    val archives = mutableListOf<Archive>()
    val screens = ArrayDeque<Screen>()
    screens.addLast(Screen.Archives)

    while (screens.isNotEmpty()) {
        when (val screen = screens.last()) {
            Screen.Archives -> showArchives(archives, screens)
            Screen.CreateArchive -> showCreateArchive(archives, screens)
            is Screen.OpenArchive -> showNotes(screen.archive, screens)
            is Screen.CreateNote -> showCreateNote(screen.archive, screens)
            is Screen.OpenNote -> showOpenNote(screen.note, screens)
            is Screen.ViewNote -> showViewNote(screen.note, screens)
        }
    }
}

private fun showArchives(archives: List<Archive>, screens: ArrayDeque<Screen>) {
    val items = buildList {
        add("Создать архив")
        addAll(archives.map { it.name })
        add("Выход")
    }
    when (val choice = showMenu("Список архивов:", items)) {
        null -> screens.clear()
        0 -> screens.addLast(Screen.CreateArchive)
        items.lastIndex -> {
            println("Выход из программы")
            screens.removeLast()
        }
        else -> screens.addLast(Screen.OpenArchive(archives[choice - 1]))
    }
}

private fun showCreateArchive(archives: MutableList<Archive>, screens: ArrayDeque<Screen>) {
    println()
    println("Создание архива")
    val name = readNonBlank(
        prompt = "Введите название архива:",
        emptyMessage = "Название архива не может быть пустым",
    ) ?: run {
        screens.clear()
        return
    }
    archives.add(Archive(name))
    screens.removeLast()
}

private fun showNotes(archive: Archive, screens: ArrayDeque<Screen>) {
    val items = buildList {
        add("Создать заметку")
        addAll(archive.notes.map { it.name })
        add("Выход")
    }
    when (val choice = showMenu("Список заметок:", items)) {
        null -> screens.clear()
        0 -> screens.addLast(Screen.CreateNote(archive))
        items.lastIndex -> screens.removeLast()
        else -> screens.addLast(Screen.OpenNote(archive.notes[choice - 1]))
    }
}

private fun showCreateNote(archive: Archive, screens: ArrayDeque<Screen>) {
    println()
    println("Создание заметки")
    val name = readNonBlank(
        prompt = "Введите название заметки:",
        emptyMessage = "Название заметки не может быть пустым",
    ) ?: run {
        screens.clear()
        return
    }
    val text = readNonBlank(
        prompt = "Введите текст заметки:",
        emptyMessage = "Текст заметки не может быть пустым",
    ) ?: run {
        screens.clear()
        return
    }
    archive.notes.add(Note(name, text))
    screens.removeLast()
}

private fun showOpenNote(note: Note, screens: ArrayDeque<Screen>) {
    val items = listOf("Посмотреть заметку", "Выход")
    when (val choice = showMenu("Открыть заметку: ${note.name}", items)) {
        null -> screens.clear()
        0 -> screens.addLast(Screen.ViewNote(note))
        else -> screens.removeLast()
    }
}

private fun showViewNote(note: Note, screens: ArrayDeque<Screen>) {
    val title = buildString {
        appendLine("Посмотреть заметку:")
        appendLine("Название: ${note.name}")
        append("Текст: ${note.text}")
    }
    when (showMenu(title, listOf("Выход"))) {
        null -> screens.clear()
        else -> screens.removeLast()
    }
}

private fun showMenu(title: String, items: List<String>): Int? {
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

private fun readNonBlank(prompt: String, emptyMessage: String): String? {
    while (true) {
        println(prompt)
        val line = readlnOrNull() ?: return null
        val value = line.trim()
        if (value.isNotEmpty()) {
            return value
        }
        println(emptyMessage)
    }
}
