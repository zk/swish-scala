/*
The MIT License

Copyright (c) 2009 Zachary Kim

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package napplelabs.swish.example

import java.util.StringTokenizer
import napplelabs.swish.{Swish, ServerConfig, ServerConnection, CommandResult}

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

