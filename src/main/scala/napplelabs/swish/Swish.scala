/*
The MIT License

Copyright (c) 2009 Zachary Kim

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/


package napplelabs.swish

import exceptions.{CommandFailedException, ConnectException}
import io.Source
import java.lang.String
import com.jcraft.jsch.{ChannelExec, Session, JSch}
import collection.Iterator
import org.slf4j.LoggerFactory

object Swish {

    val log = LoggerFactory.getLogger(this.getClass)

    def exec(command: String): Process = {
        val commandList = new java.util.ArrayList[String]()
        command.split("\\s+").foreach(commandList.add(_))

        val pb = new ProcessBuilder(commandList)
        pb.redirectErrorStream(true)

        try {
            val p = pb.start
            p.waitFor
            val lines: Iterator[String] = Source.fromInputStream(p.getInputStream).getLines()
            val output: String = if (lines.hasNext) {
                lines.reduceLeft {_ + "\n" + _}
            } else {
                ""
            }

            if (p.exitValue != 0) throw new CommandFailedException("Command returned non-zero: " + output)

            new Process(output, p.exitValue)
        } catch {
            //case e => new Process(e.getMessage, 2)
            case e => throw new CommandFailedException("Command `" + command + "` failed: " + e.getMessage)
        }
    }

    def execWithMonitor(command: String, f: (String) => Any): ProcessMonitor = {
        val commandList = new java.util.ArrayList[String]()
        command.split("\\s+").foreach(commandList.add(_))

        val pb = new ProcessBuilder(commandList)
        pb.redirectErrorStream(true)

        try {
            val p = pb.start

            val s = Source.fromInputStream(p.getInputStream)

            val pm = new ProcessMonitor(p, s, f)

            new Thread(pm).start

            Runtime.getRuntime.addShutdownHook(new Thread(new Runnable() {
                def run = {
                    log.debug("Killing process")
                    pm.exit
                }
            }))

            pm
        } catch {
            //case e => new Process(e.getMessage, 2)
            case e => throw new CommandFailedException("Command `" + command + "` failed: " + e.getMessage)
        }
    }

    def withServer[A](sc: ServerConfig)(f: (ServerConnection) => A): A = {

        val sch = new JSch()
        sch.addIdentity(sc.privateKey)
        sch.setKnownHosts("/Users/zkim/.ssh/known_hosts")
        val sess: Session = sch.getSession(sc.user, sc.host, 22)
        //sess.setPassword(sc.password)
        try {
            sess.connect
            val out = f(new ServerConnection(sess, sc))
            sess.disconnect

            out
        } catch {
            case e if (e.getMessage.contains("Auth fail")) =>
                throw new ConnectException("Couldn't connect with the following server config: " + sc)
            case e if (e.getMessage.contains("Connection refused")) =>
                throw new ConnectException("Connection refused for server config: " + sc)
            case e:Exception => {
                println("MESSAGE: " + e.getMessage)
                throw e
            }
        } finally {
            if (sess.isConnected()) {
                sess.disconnect
            }
        }
    }
}

class ProcessMonitor(p: java.lang.Process, s: Source, f: (String) => Any) extends Runnable {
    var done = false

    def run: Unit = {
        while (true) {
            s.getLines().foreach {
                line =>

                    if (!done) {
                        f(line)
                    } else {
                        p.destroy
                        return null
                    }
            }
        }

    }

    def exit = {       
        done = true
    }

}

class ServerConnection(private val session: Session, val sc: ServerConfig) {

    private var _report = List[String]()
    def report = _report.reverse

    def exec(command: String): CommandResult = {
        val c = session.openChannel("exec")

        c.asInstanceOf[ChannelExec].setCommand(command)

        c.connect
        val is = c.getInputStream
        val source = Source.fromInputStream(is)
        val output = source.mkString

        c.disconnect

        if (c.getExitStatus != 0) {
            throw new CommandFailedException("Command `" + command + "` failed with output: " + output)
        }

        c.getExitStatus match {
            case 0 => _report = ("SUCCESS " + command) :: _report
            case _ => _report = ("FAILURE " + command) :: _report
        }

        CommandResult(output, c.getExitStatus)
    }

    def exec(commands: String*): List[CommandResult] = commands.map(exec(_)).toList

    def exec(commands: Iterable[String]): List[CommandResult] = commands.map(exec(_)).toList
}

case class CommandResult(output: String, exitValue: Int) {
    val successful = exitValue == 0
}

case class ServerConfig(
        user: String = "root",
        host: String = "localhost",
        password: String = "",
        privateKey: String = System.getProperty("user.home") + "/.ssh/id_rsa",
        knownHostsPath: String = System.getProperty("user.home") + "/.ssh/known_hosts",
        port: Int = 22
        )

case class Process(output: String, exitValue: Int)




