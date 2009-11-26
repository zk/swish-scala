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

import exceptions.ConnectException
import io.Source
import java.lang.String
import com.jcraft.jsch.{ChannelExec, Session, JSch}

object Swish {
    def exec(command: String): Process = {
        val commandList = new java.util.ArrayList[String]()
        command.split("\\s+").foreach(commandList.add(_))

        val pb = new ProcessBuilder(commandList)
        pb.redirectErrorStream(true)

        try {
            val p = pb.start
            p.waitFor
            val output = Source.fromInputStream(p.getInputStream).toString
            new Process(output, p.exitValue)
        } catch {
            case e => new Process(e.getMessage, 2)
        }
    }

    def withServer(sc: ServerConfig)(f: (ServerConnection) => Any) {

        def stuff = println("")

        val sch = new JSch()
        sch.addIdentity(sc.privateKey)
        sch.setKnownHosts("/Users/zkim/.ssh/known_hosts")
        val sess: Session = sch.getSession(sc.user, sc.host, 22)
        //sess.setPassword(sc.password)
        try {
            sess.connect
            f(new ServerConnection(sess))

            sess.disconnect
        } catch {
            case e if (e.getMessage == "Auth fail") =>
                throw new ConnectException("Couldn't connect with the following server config: " + sc) 
        } finally {
            if(sess.isConnected()) {
                sess.disconnect
            }
        }
    }
}

class ServerConnection(private val session: Session) {
    def exec(command: String): CommandResult = {
        val c = session.openChannel("exec")
        c.asInstanceOf[ChannelExec].setCommand(command)

        c.connect

        val is = c.getInputStream

        val source = Source.fromInputStream(is)
        val output = source.mkString

        c.disconnect

        CommandResult(output, c.getExitStatus)
    }
}

case class CommandResult(output: String, exitValue: Int)

case class ServerConfig(
        user: String                = "root",
        host: String                = "localhost",
        password: String            = "",
        privateKey: String          = System.getProperty("user.home") + "/.ssh/id_rsa",
        knownHostsPath: String      = System.getProperty("user.home") + "/.ssh/known_hosts",
        port: Int                   = 22
        )

case class Process(output: String, exitValue: Int)

class Command(command: String, serverConn: ServerConnection) {
    val res = serverConn.exec(command)
}