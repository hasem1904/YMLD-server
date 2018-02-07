package no.sintrasoft.akka.http.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives.{complete, get, path, pathPrefix, respondWithHeaders}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.google.inject.Inject
import no.sintrasoft.akka.http.serialization.JsonSerialization
import no.sintrasoft.controller.TransactionController
import no.sintrasoft.logger.Logger

class TransactionRoutes @Inject()(controller: TransactionController, implicit val materializer: ActorMaterializer) extends Logger with JsonSerialization  {
  //Transaction routes
  def apply(): Route = {
    pathPrefix("transactionlog") {
      respondWithHeaders(RawHeader("Access-Control-Allow-Origin", "*")) {
        path("all") { get { complete { controller.getAllTransactionLogs}}}}
      }
  }
}
