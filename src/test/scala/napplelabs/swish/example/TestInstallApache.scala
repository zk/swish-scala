package napplelabs.swish.example

import org.junit.Test
import org.junit.Assert._
import napplelabs.swish.{ServerConfig, Swish}
import napplelabs.swish.tasks.{YumInstallTask, StringTaskReportFormatter, PrintlnReportFormatter, InstallApache}

class TestInstallApache {
    @Test
    def testInstallApache = {
        val res = Swish.withServer(ServerConfig(user = "root", host = "173.203.211.254")) {
            sc =>
                val taskResult = sc.execTask(new YumInstallTask("httpdasdf"))                
                println(StringTaskReportFormatter.format(taskResult))
        }
    }
}