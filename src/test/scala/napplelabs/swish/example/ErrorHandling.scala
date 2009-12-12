package napplelabs.swish.example

import napplelabs.swish.{ServerConnection, Swish, ServerConfig}

object ErrorHandling {
    def main(args: Array[String]) = {
        val sc = ServerConfig(user = "zkim")
        Swish.withServer(sc) {
            conn =>
                conn.exec(
                    "ls -aul",
                    "cat asdfasdf"
                    )


        }
    }
}