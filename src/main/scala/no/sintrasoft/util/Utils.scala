package no.sintrasoft.util

import no.sintrasoft.ProductOperations.actorSystem
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.Source

object Utils {
  /**
    * Prints the banner
    */
  def printBanner(appName:String, appVer: String, env: String, scalaVer: String, akkaVer: String): Unit = {
    val applicationName = "{info.app.name}"
    val applicationVersion = "{application.version}"
    val applicationEnvironment = "{application.environment}"
    val scalaVersion = "{scala.version}"
    val akkaVersion = "{akka.version}"

    Source.fromInputStream(this.getClass.getResourceAsStream("/banner.txt"))
      .getLines()
      .foreach {
        case line@l if l.contains(applicationName) => println(line.replace(applicationName, appName))
        case line@l if l.contains(applicationVersion) => println(line.replace(applicationVersion, appVer))
        case line@l if l.contains(applicationEnvironment) => println(line.replace(applicationEnvironment, env))
        case line@l if l.contains(scalaVersion) => println(line.replace(scalaVersion, scalaVer))
        case line@l if l.contains(akkaVersion) => println(line.replace(akkaVersion, akkaVer))
        case l => println(l)
      }
  }

  /**
    * Shuts down the application and the Akka system.
    */
  def shutdown(): Unit = {
    println("\n> Ymld App done. Shutting down the program.")
    //Shutting down the akka system.
    val shutdown = actorSystem.terminate()
    Await.result(shutdown, 1.minute)
    System.exit(-1)
  }
}
