package napplelabs.swish.tasks

import napplelabs.swish.{CommandResult, ServerConnection}

sealed abstract class TaskResultStatus
case object SUCCEEDED extends TaskResultStatus
case object FAILED extends TaskResultStatus
case object NOOP extends TaskResultStatus


case class TaskResult(name: String, desc: String, errors: List[CommandResult], status: TaskResultStatus)

abstract class Task {
    def name: String

    def exec(sc: ServerConnection) : TaskResult

    private var _errors = List[CommandResult]()

    protected def addError(cr: CommandResult) = _errors = cr :: _errors

    def errors: List[CommandResult] = _errors.reverse
}

object EmptyTask extends Task {
    def name = "Empty Task"

    def exec(sc: ServerConnection) = TaskResult("", "", List[CommandResult](), NOOP)    
}


class InstallApache extends Task {
    def description = "Installs the httpd package through yum."

    def name = "Install Apache Httpd"

    def exec(sc: ServerConnection) : TaskResult = {
        if (!sc.exec("yum list installed httpd").succeeded) {
            val installRes = sc.exec("yum -y install httpdasdf")
            if(installRes.output.contains("Nothing to do")) {
                addError(installRes)
                TaskResult(name, "Package not found", errors, FAILED)
            } else {
                TaskResult(name, "", List[CommandResult](), SUCCEEDED)
            }
        } else {
            TaskResult(name, "HTTPD already present", List[CommandResult](), NOOP)
        }
    }
}

class YumInstallTask(val pkg: String) extends Task {
    def name = "Yum Install " + pkg 
    
    def exec(sc: ServerConnection) : TaskResult = {
        if (!sc.exec("yum list installed " + pkg).succeeded) {
            val installRes = sc.exec("yum -y install " + pkg)
            if(installRes.output.contains("Nothing to do")) {
                addError(installRes)
                TaskResult(name, "Package " + pkg + " not found", errors, FAILED)
            } else {
                TaskResult(name, "", List[CommandResult](), SUCCEEDED)
            }
        } else {
            TaskResult(name, "Package " + pkg + " already present", List[CommandResult](), NOOP)
        }
    }
}