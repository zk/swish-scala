package napplelabs.swish.commands

import napplelabs.swish.ServerConnection
import java.util.UUID

/**
 * Command to tar/gzip a remote directory for scp transfer
 */
object GZipDirectory {
    def apply(path: String, ssh: ServerConnection): String = {
        val randomName = UUID.randomUUID.toString
        val tempPath = "/tmp/" + randomName
        val tempTarGz = tempPath + ".tar"

        ssh.exec("cd " + path + " && tar -cvf " + tempTarGz + " ./")
        ssh.exec("gzip " + tempTarGz)
        
        tempTarGz + ".gz"
    }
}