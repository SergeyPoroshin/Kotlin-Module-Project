fun showNotes(archive: Archive, screens: ArrayDeque<Screen>) {
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

fun showCreateNote(archive: Archive, screens: ArrayDeque<Screen>) {
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

fun showOpenNote(note: Note, screens: ArrayDeque<Screen>) {
    val items = listOf("Посмотреть заметку", "Выход")
    when (val choice = showMenu("Открыть заметку: ${note.name}", items)) {
        null -> screens.clear()
        0 -> screens.addLast(Screen.ViewNote(note))
        else -> screens.removeLast()
    }
}

fun showViewNote(note: Note, screens: ArrayDeque<Screen>) {
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
