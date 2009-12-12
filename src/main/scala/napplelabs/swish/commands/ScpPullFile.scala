package napplelabs.swish.commands

import napplelabs.swish.{ServerConfig, Swish}

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Nov 30, 2009
 * Time: 1:43:00 AM
 * To change this template use File | Settings | File Templates.
 */

object ScpPullFile {
    def apply(remoteFile: String, localPath: String, sc: ServerConfig) = {
        Swish.exec("scp -i " + sc.privateKey + " " + sc.user + "@" + sc.host + ":" + remoteFile + " " + localPath )
    }
}