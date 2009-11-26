package napplelabs.swish.example

import napplelabs.swish._


object Simple {
    def main(args: Array[String]) = {
        val sc = new ServerConfig(user = "zkim")

        Swish.withServer(sc) {
            conn =>
            val commandResponse = conn.exec("ls -aul")
            println("Exit Value: " + commandResponse.exitValue)
            println("Output: " + commandResponse.output)
        }
    }
}

