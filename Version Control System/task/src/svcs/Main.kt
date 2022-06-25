package svcs

import java.io.File

val vcsRoot = File(".")
val vcsDir = File("./vcs")
val configFile = File("./vcs/config.txt")
val indexFile = File("./vcs/index.txt")
var userName = ""

fun main(args: Array<String>) {
//    while (true) {
    // init create index.txt and config.txt
    if (!vcsDir.exists()) vcsDir.mkdir()
    if (!indexFile.exists()) indexFile.createNewFile()
    if (!configFile.exists()) configFile.createNewFile()

    if (args.size > 0) {
        when (args[0]) {
            "--help" -> infoHelp(args[0])
            "config" -> config(args)
            "add" -> add(args)
            "log" -> log(args)
            "commit" -> commit(args)
            "checkout" -> checkout(args)
            else -> println("'${args[0]}' is not a SVCS command.")
        }
    } else
        infoHelp("")
    /*else if (args.size == 1) {
        infoHelp(args[0])
    } else {
        infoHelp("")
    }*/

//        if (input == "0") break
//    }
}

fun checkout(args: Array<String>) {
    println("Restore a file.")
}

fun commit(args: Array<String>) {
    println("Save changes.")
}

fun log(args: Array<String>) {
    println("Show commit logs.")
}
/*val vcsRoot = File(".")
val vcsDir = File("./vcs")
val configFile = File("./vcs/config.txt")
val indexFile = File("./vcs/index.txt")
var userName=""*/

fun add(args: Array<String>) {
    if (args.size == 1 && indexFile.length() == 0L) {
        println("Add a file to the index.")
    } else if (args.size == 1) {
        println("Tracked files:")
        for (line in indexFile.readLines()) println(line)
    } else if (args.size == 2) {
        val fileNameString = "./" + args[1]
        if (!File(fileNameString).exists()) {
            println("Can't find '${args[1]}'.")
        } else {
            indexFile.appendText(args[1])
            indexFile.appendText("\n")
            println("The file '${args[1]}' is tracked.")
        }
    }
}

fun config(args: Array<String>) {
    if (args.size == 1 && configFile.length() == 0L) {
        // infoHelp(args[0])
        println("Please, tell me who you are.")
    } else if (args.size == 1) {
        println("The username is ${configFile.readText()}.")
    } else if (args.size == 2) {
        // new user name will be saved to config file
        // write with append command a file
        configFile.writeText(args[1])
        println("The username is ${args[1]}.")
    }
}

fun infoHelp(help: String) {
    //println("infoHelp is called with $help")
    val __help = """
        These are SVCS commands:
        config     Get and set a username.
        add        Add a file to the index.
        log        Show commit logs.
        commit     Save changes.
        checkout   Restore a file.
    """.trimIndent()
    val config = "Get and set a username."
    val add = "Add a file to the index."
    val log = "Show commit logs."
    val commit = "Save changes."
    val checkout = "Restore a file."

    when (help) {
        "", "--help" -> println(__help)
        "config" -> {
            println(config)
        }
        "add" -> println(add)
        "log" -> println(log)
        "commit" -> println(commit)
        "checkout" -> println(checkout)
        else -> println("'$help' is not a SVCS command.")
    }
}

enum class argsEnum {
    __HELP,
    CONFIG,
    ADD,
    LOG,
    COMMIT,
    CHECKOUT
}