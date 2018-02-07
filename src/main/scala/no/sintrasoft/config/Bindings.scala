package no.sintrasoft.config

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContext

class Bindings extends AbstractModule {
  val service = "Your-Momma-Loves-Drama-Microservice"
  val serviceId = "YMLD"
  val serviceName = "Your-Momma-Loves-Drama"

  override def configure(): Unit = {
    bindNames()
    bindConfig()
    bindAkkaActorSystem()
  }

  def bindNames(): Unit = {
    bind(classOf[String]).annotatedWith(Names.named("service")).toInstance(service)
    bind(classOf[String]).annotatedWith(Names.named("serviceId")).toInstance(serviceId)
    bind(classOf[String]).annotatedWith(Names.named("serviceName")).toInstance(serviceName)
  }

  def bindConfig(): Unit = {
    val config = ConfigFactory.load()
    bind(classOf[Config]).toInstance(config)
    //health.disk.threshold
    val threshold = config.getLong("health.disk.threshold")
    bind(classOf[Long]).annotatedWith(Names.named("threshold")).toInstance(threshold)
    //info.app.name
    val name = config.getString("info.app.name")
    bind(classOf[String]).annotatedWith(Names.named("name")).toInstance(name)
    //info.app.description
    val description = config.getString("info.app.description")
    bind(classOf[String]).annotatedWith(Names.named("description")).toInstance(description)
    //info.app.version
    val version = config.getString("info.app.version")
    bind(classOf[String]).annotatedWith(Names.named("version")).toInstance(version)
    //info.app.environment
    val environment = config.getString("info.app.environment")
    bind(classOf[String]).annotatedWith(Names.named("environment")).toInstance(environment)
  }

  def bindAkkaActorSystem(): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem(s"$serviceName-actor-system")
    bind(classOf[ActorSystem]).toInstance(actorSystem)
    bind(classOf[ExecutionContext]).toInstance(actorSystem.dispatcher)
    bind(classOf[ActorMaterializer]).toInstance(ActorMaterializer())
  }
}
