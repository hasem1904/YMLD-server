package no.sintrasoft.akka.http.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.{Inject, Singleton}
import no.sintrasoft.akka.http.serialization.JsonSerialization
import no.sintrasoft.controller.PricePlanController
import no.sintrasoft.domain.PricePlanRequest
import no.sintrasoft.logger.Logger

@Singleton
class PricePlanRoutes @Inject()(controller: PricePlanController, implicit val materializer: ActorMaterializer) extends Logger with JsonSerialization{
  def apply(): Route = {
    pathPrefix("priceplan") {
      (post & entity(as[PricePlanRequest])) { pricePlanRequest => complete(controller.createPricePlan(pricePlanRequest)) } ~
        path(LongNumber) { id => {
          (put & entity(as[PricePlanRequest])) { pricePlanRequest => complete(controller.updatePricePlan(id, pricePlanRequest)) } ~
            get {complete{controller.getPricePlan(id)}} ~
            delete {complete{controller.deletePricePlan(id)}}}} ~
        path("all") {
          respondWithHeaders(RawHeader("Access-Control-Allow-Origin", "*")) {
            get {complete {controller.getAllPricePlans}}
          }
        }
    }
  }
}
