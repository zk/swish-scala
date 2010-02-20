package napplelabs.swish.example

import napplelabs.swish.{ServerConfig, Swish}
import org.junit.Test
import org.junit.Assert._

class ClosureReturnTypeExample {
    
    def runClosure[T](f: Any => T) = {
        f()
    }

    @Test
    def testMain() {       
        val res2 = Swish.withServer(ServerConfig(user = "zkim")) {
            ssh =>
                ssh.exec("ls -aul").output                
        }

        assertTrue(res2.getClass == classOf[String])
        assertTrue(res2.size > 0)

        Swish.withServer(ServerConfig(user = "zkim")) {
            ssh =>
                ssh.exec("ls -aul")
                ssh.exec("ls -aul")
                ssh
        }
    }
}