package napplelabs.swish.example

import napplelabs.swish.{ServerConfig, Swish}
import org.junit.Test
import org.junit.Assert._

class ClosureReturnTypeExample {

    @Test
    def testMain() {
        val res = Swish.withServer(ServerConfig(user = "zkim")) {
            ssh =>
                ssh.exec("ls -aul")
        }

        assertTrue(res.succeeded)


        val res2 = Swish.withServer(ServerConfig(user = "zkim")) {
            ssh =>
                ssh.exec("ls -aul")
                ssh.exec("cat asdf")
        }

        assertFalse(res2.succeeded)
    }
}