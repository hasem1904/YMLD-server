package no.sintrasoft.akka.http.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.{Inject, Singleton}
import no.sintrasoft.akka.http.serialization.JsonSerialization
import no.sintrasoft.controller.SeatMapController
import no.sintrasoft.logger.Logger

@Singleton
class SeatMapRoutes @Inject()(controller: SeatMapController, implicit val materializer: ActorMaterializer) extends Logger with JsonSerialization {
  def apply(): Route = {
    pathPrefix("seatmap") {
    path(LongNumber) { id => { get {complete {controller.getSeatMap(id)}}}} ~
    path("all") { get { complete { controller.getAllSeatMaps}}}}
  }
}
