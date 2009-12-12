package napplelabs.swish.example.commands

import napplelabs.swish.{ServerConfig, Swish}
import java.io.File
import napplelabs.swish.commands.{DownloadDirectoryResult, DownloadDirectory, ScpPullFile, GZipDirectory}

object DownloadDirectoryExample {
    def main(args: Array[String]) : Unit = {

        val sc = ServerConfig(
            user = "zkim"
            )

        val remotePath = "/var/log/apache2"

        var ddr : DownloadDirectoryResult = null

        Swish.withServer(sc) {
            ssh =>

                ddr = DownloadDirectory(ssh, remotePath)

        }
        val localPath = ddr.localPath
        val localFile = new File(localPath)
        println(localPath)
        assert(localFile.exists)
        assert(localFile.isDirectory)

        //Swish.exec("rm -rf " + localPath)
    }
}