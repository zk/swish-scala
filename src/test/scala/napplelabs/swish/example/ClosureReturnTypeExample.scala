package napplelabs.swish.example

import napplelabs.swish.{ServerConfig, Swish}
import org.junit.Test
import org.junit.Assert._
import napplelabs.swish.tasks.PrintlnReportFormatter

class ClosureReturnTypeExample {

    @Test
    def testMain() {
        Swish.withServer(ServerConfig(user = "zkim")) {
            ssh =>
                ssh.exec("ls -aul")
        }

        Swish.withServer(ServerConfig(user = "zkim")) {
            ssh =>
                ssh.exec("ls -aul")
                val r = ssh.exec("cat asdf")
                assertTrue(r.error != "")                
        }
    }
}