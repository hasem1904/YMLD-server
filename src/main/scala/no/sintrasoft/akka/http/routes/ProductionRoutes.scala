package no.sintrasoft.akka.http.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.{Inject, Singleton}
import no.sintrasoft.akka.http.serialization.JsonSerialization
import no.sintrasoft.controller.ProductionController
import no.sintrasoft.domain.ProductionRequest
import no.sintrasoft.logger.Logger

/**
  * Custom Json4s support:
  * https://danielasfregola.com/2015/08/17/spray-how-to-deserialize-entities-with-json4s/
  * https://doc.akka.io/docs/akka-http/current/scala/http/common/json-support.html
  * https://github.com/json4s/json4s/issues/173
  * https://github.com/json4s/json4s/issues/388
  *
  * @param controller
  * @param materializer
  */
@Singleton
class ProductionRoutes @Inject()(controller: ProductionController, implicit val materializer: ActorMaterializer) extends Logger with JsonSerialization{
  def apply(): Route = {
    pathPrefix("production") {
      (post & entity(as[ProductionRequest])) { productionRequest => complete(controller.createProduction(productionRequest)) } ~
        path(LongNumber) { id => {
         (put & entity(as[ProductionRequest])) { productionRequest => complete(controller.updateProduction(id, productionRequest)) } ~
          get {complete{controller.getProduction(id)}} ~
          delete {complete{controller.deleteProduction(id)}}}} ~
        path("all") {
          get {complete {controller.getAllProductions}}}}
  }
}
