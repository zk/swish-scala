package napplelabs.swish.tasks

import napplelabs.swish.ServerConnection

abstract class Task {
    def exec(sc: ServerConnection)
}


object InstallApache extends Task {
    def exec(sc: ServerConnection) = {
        sc.exec("yum list installed httpd")
        //        if(!sc.exec("yum list installed httpd").succeeded) {
        //            sc.exec("yum -y install httpd")
        //        }
    }
}