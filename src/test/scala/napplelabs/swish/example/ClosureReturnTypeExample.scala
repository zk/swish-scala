package napplelabs.swish.example

import napplelabs.swish.{ServerConfig, Swish}

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Dec 1, 2009
 * Time: 9:53:40 PM
 * To change this template use File | Settings | File Templates.
 */

object ClosureReturnTypeExample {
    def runClosure[T](f: Any => T) = {
        f()
    }

    def main(args: Array[String]): Unit = {
        val res = runClosure({
            x =>
                "asdf"
        })


        val res2 = Swish.withServer(ServerConfig(user = "zkim")) {
            ssh => List[String]()
        }

        println(res2.getClass)


    }
}