Swish - A scala library for ssh automation
==========================================

Alpha quality. Expect api changes, etc.

Inspired by ruby's Net:SSH (http://net-ssh.rubyforge.org/)

MIT Licensed (http://www.opensource.org/licenses/mit-license.php)

Basic Usage:
<pre>
/*
  Server config to connect zkim@localhost with password 'mypass'
*/
val serverConfig = ServerConfig(host = "localhost", user = "zkim", password = "mypass")
Swish.withServer(serverConfig) {
    conn =>
        println(conn.exec("ls -aul").output)
}
</pre>


See /test/napplelabs/swish/example for more examples, specifically:

### Simple scala
Dead-simple connect and execute command example.

### CustomCommands.scala
Modularization of exec, commands encapsulate error handling and marshalling.  I'm still trying to figure out what the api for this should look like.

### On Deck
A better dsl


new Stuff(