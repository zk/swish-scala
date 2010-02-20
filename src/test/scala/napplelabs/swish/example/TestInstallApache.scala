package napplelabs.swish.example

import org.junit.Test
import org.junit.Assert._
import napplelabs.swish.{ServerConfig, Swish}
import napplelabs.swish.tasks.InstallApache

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Feb 19, 2010
 * Time: 11:47:29 PM
 * To change this template use File | Settings | File Templates.
 */

class TestInstallApache {
    @Test
    def testInstallApache = {
        val res = Swish.withServer(ServerConfig(user = "root", host = "173.203.211.254")) {
            sc =>
                InstallApache.exec(sc)
        }

        println(res.report)
        assertTrue(res.succeeded)

    }
}