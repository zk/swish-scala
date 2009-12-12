package napplelabs.swish.example

import napplelabs.swish.Swish

object ExecWithMonitorExample {
    def main(args: Array[String]) {

        val ctrl = Swish.execWithMonitor("ping google.com", {
            line => println(line)
        })

        Thread.sleep(10000)

        ctrl.exit
    }
}