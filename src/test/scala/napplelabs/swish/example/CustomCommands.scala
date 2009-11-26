package napplelabs.swish.example

import java.util.StringTokenizer
import napplelabs.swish.{Swish, ServerConfig, ServerConnection, CommandResult}

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Nov 25, 2009
 * Time: 7:29:21 PM
 * To change this template use File | Settings | File Templates.
 */

object CustomCommands {
    object DfCommand {
        def apply(sc: ServerConnection): List[DfResult] = {
            val res = sc.exec("df -hl")
            res.output.split("\\n").tail.map {
                line =>
                    val tokenizer = new StringTokenizer(line, " ")
                    DfResult(
                        tokenizer.nextToken,
                        tokenizer.nextToken,
                        tokenizer.nextToken,
                        tokenizer.nextToken,
                        java.lang.Double.parseDouble(tokenizer.nextToken.replace("%", "")) / 100,
                        tokenizer.nextToken
                        )
            }.toList
        }
    }

    case class DfResult(fileSystem: String, size: String, used: String, available: String, usePercent: Double, mountedOn: String)

    object LsCommand {
        val command = "ls -aul"

        def apply(sc: ServerConnection): (Int, CommandResult) = {
            val res = sc.exec(command)
            val total = Integer.parseInt(res.output.split("\\n")(0).split(" ")(1))

            (total, res)
        }
    }

    def main(args: Array[String]) = {
        val sc = new ServerConfig(user = "zkim")
        Swish.withServer(sc) {
            conn =>
                val partitions = DfCommand(conn)
                println(partitions)
        }
    }
}

