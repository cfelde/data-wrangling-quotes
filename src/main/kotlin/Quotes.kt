import sigbla.app.*
import sigbla.widgets.*
import java.io.File
import kotlinx.html.img
import kotlinx.html.style

fun main() {
    TableView[Port] = 8080

    val quotesFile = File("quotes/output")

    val separator = "-------------"
    val currentBatch = mutableListOf<String>()
    val batches = mutableListOf<List<String>>()

    // Load quotes file and separate individual quotes
    quotesFile.bufferedReader().lineSequence().forEach {
        if (it == separator) {
            batches += currentBatch.toList()
            currentBatch.clear()
        } else {
            currentBatch += it
        }
    }

    // Create table and table view
    val table = Table["quotes"]
    val tableView = TableView[table]

    // Set width of columns
    tableView["Text"][CellWidth] = 700
    tableView["Img"][CellWidth] = 300

    // For saving output
    val quotesTableFile = File("quotes.sigt")

    // Load any existing table file
    if (quotesTableFile.exists()) {
        load(quotesTableFile to table)
    }

    batches.forEachIndexed { index, strings ->
        val text = strings.toMutableList()
        while (text.isNotEmpty() && text.last().isBlank()) text.removeLast()
        val img = text.removeLast().replace(".txt", "")

        if (table["Text", index] !is StringCell || table["Text", index].toString().isBlank()) {
            // Do some basic tidying
            val tidyText = text.map { it.trim() }.filter { it.isNotBlank() }.toMutableList()
            val author = tidyText.removeLast().trim()

            // Replace some common issues
            val quote = (tidyText.joinToString(" ").trim() + " - " + author)
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("—", "-")
                .replace("’", "'")

            // Add quote to text column
            table["Text", index] = quote
        }

        // Add image to image column + css to scale down images
        table["Img", index] = div {
            img {
                src = "img/$img"
                style = """
                    height: 70%;
                    width: 70%;
                """.trimIndent()
            }
        }

        // Add text field to table view text column, and pass any updated on to table + save updates
        tableView["Text", index] = textField(table["Text", index].toString()) {
            table["Text", index] = this.text
            save(table to quotesTableFile)
            println("Saved change: ${table["Text", index]}")
        }

        // Set height for image
        tableView[index][CellHeight] = 200

        // Serve image as resource
        tableView[Resources] = tableView[Resources] + ("img/$img" to staticFile(File(quotesFile.parentFile, img)))
    }

    // Show and print URL, so we can open it in browser
    println(show(tableView))
}