package napplelabs.swish.commands

import napplelabs.swish.{Swish, ServerConfig, ServerConnection}
import java.util.UUID

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Nov 30, 2009
 * Time: 7:54:07 PM
 * To change this template use File | Settings | File Templates.
 */

object DownloadDirectory {
    def apply(ssh: ServerConnection, remotePath: String, localPath: String = "/tmp/" + UUID.randomUUID.toString) = {
        val gzipPath = GZipDirectory(remotePath, ssh)
        val localGzipPath = "/tmp/" + UUID.randomUUID.toString + ".tar.gz"
        ScpPullFile(gzipPath, localGzipPath, ssh.sc)
        ssh.exec("rm -f " + gzipPath)
        Swish.exec("mkdir " + localPath)
        Swish.exec("tar -xvf " + localGzipPath + " -C " + localPath)
        Swish.exec("rm -f " + localGzipPath)

        DownloadDirectoryResult(localPath)
    }
}

case class DownloadDirectoryResult(localPath: String)