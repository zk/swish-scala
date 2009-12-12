package napplelabs.swish.example.commands

import napplelabs.swish.{ServerConfig, Swish}
import napplelabs.swish.commands.{ScpPullFile, GZipDirectory}
import java.io.File

object GZipDirExample {
    def main(args:Array[String]) = {

        def sc = ServerConfig(
            user = "zkim"
            )

        Swish.withServer(sc) {
            ssh =>
            val gzipPath = GZipDirectory("/var/log/apache2", ssh)
            println(gzipPath)

            Swish.exec("mkdir /tmp/gziptemp")
            ScpPullFile(gzipPath, "/tmp/gziptemp/myfile.tar.gz", sc)
            Swish.exec("tar -xvf /tmp/gziptemp/myfile.tar.gz -C /tmp/gziptemp")

            assert(new File("/tmp/gziptemp/myfile.tar.gz").exists)

            //clean up
            Swish.exec("rm -rf /tmp/gziptemp")
        }
    }
}