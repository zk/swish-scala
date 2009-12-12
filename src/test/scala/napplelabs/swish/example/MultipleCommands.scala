package napplelabs.swish.example

import napplelabs.swish.{ServerConfig, Swish}

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Nov 25, 2009
 * Time: 8:50:02 PM
 * To change this template use File | Settings | File Templates.
 */

object MultipleCommands {
    def main(args: Array[String]) = {
        Swish.withServer(ServerConfig("zkim")) {
            conn =>
                val results = conn.exec(
                    "ls -aul",
                    "df -h"
                    )

                println(results)
        }
    }
}