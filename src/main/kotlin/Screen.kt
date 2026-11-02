sealed interface Screen {
    data object Archives : Screen
    data object CreateArchive : Screen
    data class OpenArchive(val archive: Archive) : Screen
    data class CreateNote(val archive: Archive) : Screen
    data class OpenNote(val note: Note) : Screen
    data class ViewNote(val note: Note) : Screen
}
