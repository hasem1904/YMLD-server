package no.sintrasoft.akka.http.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.{Inject, Singleton}
import no.sintrasoft.akka.http.serialization.JsonSerialization
import no.sintrasoft.controller.PerformanceController
import no.sintrasoft.domain.PerformanceRequest
import no.sintrasoft.logger.Logger

/**
  * https://blog.scalents.com/2017/08/21/how-to-create-an-akka-http-server/
  * @param controller
  */
@Singleton
class PerformanceRoutes @Inject()(controller: PerformanceController, implicit val materializer: ActorMaterializer) extends Logger with JsonSerialization {
  def apply(): Route = {
    pathPrefix("performance") {
      (post & entity(as[PerformanceRequest])) { performanceRequest => complete(controller.createPerformance(performanceRequest)) } ~
      path(LongNumber) { id => { get {complete {controller.getPerformance(id)}} ~
        (put & entity(as[PerformanceRequest])) { performanceRequest => complete(controller.updatePerformance(id, performanceRequest)) } ~
        get {complete{controller.getPerformance(id)}} ~
        delete {complete{controller.deletePerformance(id)}}}} ~
      path("all") {
        respondWithHeaders(RawHeader("Access-Control-Allow-Origin", "*")){
          get {complete{controller.getAllPerformances}}}}
        }
  }
}