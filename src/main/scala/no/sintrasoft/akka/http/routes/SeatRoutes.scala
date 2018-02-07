package no.sintrasoft.akka.http.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.{Inject, Singleton}
import no.sintrasoft.akka.http.serialization.JsonSerialization
import no.sintrasoft.controller.SeatController
import no.sintrasoft.logger.Logger

@Singleton
class SeatRoutes @Inject()(controller: SeatController, implicit val materializer: ActorMaterializer) extends Logger with JsonSerialization {
  //Seat routes
  def apply(): Route = {
    pathPrefix("seat") {
      path(LongNumber/"performance"/LongNumber) { (id, performanceId) => { get { complete { controller.getAllSeatByIdAndPriceplan(id, performanceId) }}}} ~
      path("all") { get { complete { controller.getAllSeats}}}}
  }
}
