package svcs

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

val rootDir = File("./")
val vcsDir = File("./vcs")
val commitsDir = File("./vcs/commits")
val configFile = File("./vcs/config.txt")
val indexFile = File("./vcs/index.txt")
val logFile = File("./vcs/log.txt")

fun main(args: Array<String>) {
    // init create index.txt and config.txt
    if (!vcsDir.exists()) vcsDir.mkdir()
    if (!commitsDir.exists()) commitsDir.mkdir()
    if (!indexFile.exists()) indexFile.createNewFile()
    if (!configFile.exists()) configFile.createNewFile()
    if (!logFile.exists()) logFile.createNewFile()

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
}

fun checkout(args: Array<String>) {
    // if commit id or commit directory has

    if (args.size > 1) {
        val newCommitDir = "${commitsDir.path}/${args[1]}"
        if (File(newCommitDir).listFiles() != null) {
            // println(File(newCommitDir).listFiles())
            // restore theese files from commitsDir to home vcs directory
            //println("${File(newDir).path} exist")
            // then copy files

            for (line in File(newCommitDir).listFiles()!!) {
                val fileOut = File("${rootDir.path}/${line.name}")
                val fileIn = File(line.path)
                if (fileOut.exists()) {
                    fileOut.delete()
                }
                if (fileIn.exists()) {
                    fileIn.copyTo(fileOut)
                }
            }
            println("Switched to commit ${args[1]}.")
        } else {
            println("Commit does not exist.")
        }
    } else {
        println("Commit id was not passed.")
    }
}

fun commit(args: Array<String>) {
    var indexedFiles = ""

    // all indexed file will have a single hash
    if (args.size > 1 && indexFile.length() > 0) {
        for (line in indexFile.readLines()) {
            if (File(line).exists()) {
                // println(line)
                indexedFiles += File(line).readText()
            }
        }
        val hashOfAll = hash(indexedFiles)
        // println(hashOfAll)
        // make a directory with this name under vcs
        val newDir = "${commitsDir.path}/$hashOfAll"
        if (File(newDir).exists()) {
            // then hashCode is equel then nothings to commit
            println("Nothing to commit.")
            return
        } else File(newDir).mkdir()
        if (File(newDir).exists()) {
            //println("${File(newDir).path} exist")
            // then copy files
            for (line in indexFile.readLines()) {
                val fileIn = File("${rootDir.path}/$line")
                val fileOut = File("$newDir/$line")
                if (fileOut.exists()) {
                    fileOut.delete()
                }
                if (fileIn.exists()) {
                    fileIn.copyTo(fileOut)
                }
            }
            // add logs
            var newlogString = """
                    commit $hashOfAll
                    Author: ${configFile.readText()}
                    ${args[1]}
                    
                """.trimIndent()
//            logFile.appendText(logString)
            newlogString += logFile.readText()
            logFile.writeText(newlogString)
            println("Changes are committed.")

            // indexFile must be cleared
            // indexFile.writeText("")
        }


    } else if (args.size > 1 && indexFile.length() == 0L) {
        println("Nothing to commit.")
    } else {
        println("Message was not passed.")
    }

}

fun log(args: Array<String>) {
    if (logFile.length() == 0L)
        println("No commits yet.")

    println(logFile.readText())
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

fun hash(input: String): String {
    val sha1 = MessageDigest.getInstance("SHA-1")

    return BigInteger(1, sha1.digest(input.toByteArray())).toString(16)
//    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}