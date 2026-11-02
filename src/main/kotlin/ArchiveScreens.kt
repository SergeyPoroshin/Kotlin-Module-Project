fun showArchives(archives: List<Archive>, screens: ArrayDeque<Screen>) {
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

fun showCreateArchive(archives: MutableList<Archive>, screens: ArrayDeque<Screen>) {
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
