import java.io.PrintStream

fun main() {
    // Тут пришлось воспользоваться нейронкой, т.к сам запутался в причинах проблемы.
    // Вместо кириллицы были битые символы в консоли IDE.
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
