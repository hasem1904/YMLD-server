package no.sintrasoft.akka.http.routes

import akka.http.scaladsl.server.Directives.{complete, get, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.{Inject, Singleton}
import no.sintrasoft.akka.http.serialization.JsonSerialization
import no.sintrasoft.controller.AkkaHttpController
import no.sintrasoft.domain.MessageRequest
import no.sintrasoft.logger.Logger

@Singleton
class SwaggerRoutes @Inject()(akkaHttpController: AkkaHttpController, implicit val materializer: ActorMaterializer) extends Logger with JsonSerialization {
  def apply(): Route = {
    pathPrefix("akka-http-microservice") {
      path("welcome") {
        {get { complete { akkaHttpController.welcome }}} ~
        {(post & entity(as[MessageRequest])) { messageRequest => complete(akkaHttpController.welcomeMessage(messageRequest))}}
      } ~
      path("client" / Segment) { ClientName => { get { complete { akkaHttpController.getClientByName(ClientName)}}}}
    }
  }
}