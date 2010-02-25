package napplelabs.swish.tasks

import napplelabs.swish.CommandResult
import collection.immutable.List


trait ReportFormatter {
    def format(report: List[CommandResult])
}

object StringTaskReportFormatter {
    def format(taskResult: TaskResult) : String = {
        val out = new StringBuilder
        val spacing = "\t\t\t\t"
        out.append(taskResult.name)
        out.append(spacing)
        out.append(taskResult.status)
        out.append(spacing)
        out.append(taskResult.desc)

        out.toString
    }
}

object PrintlnReportFormatter extends ReportFormatter {
    def format(report: List[CommandResult]) = {

        val spacing = "\t\t\t\t\t"

        val header = "Command" + spacing + "Output"+spacing+"Error"+spacing+"Exit Code\n" +
                "-------"+spacing+"------"+spacing+"-----"+spacing+"---------"

        println(header)
        if(report.size == 0) {
            println("\t\t No Results to Report")
        } else {
            report.foreach {
                res =>

                   val commandMaxSize = 10
                   val command = if(res.command.size > commandMaxSize) {
                       res.command.substring(0, commandMaxSize)
                   } else if(res.command.size == 0) {
                       " "
                   } else {
                       res.command
                   }

                   val outputMaxSize = 10
                   val output = if(res.output.size > outputMaxSize) {
                       res.output.substring(0, outputMaxSize)
                   } else if(res.output.size == 0) {
                       " "
                   } else {
                       res.output
                   }

                   val errMaxSize = 10
                   val error = if(res.error.size > errMaxSize) {
                       res.error.substring(0, errMaxSize)
                   } else if(res.error.size == 0) {
                       " "
                   } else {
                       res.error
                   }

                   val out = command + spacing +
                             output + spacing +
                             error + spacing +
                             res.exitValue
                   println(out)
            }
        }

    }
}