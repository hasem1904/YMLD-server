package no.sintrasoft.swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import io.swagger.models.ExternalDocs
import io.swagger.models.auth.BasicAuthDefinition
import no.sintrasoft.controller._

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses = Set(classOf[AkkaHttpController],
    classOf[PerformanceController],
    classOf[PricePlanController],
    classOf[ProductionController],
    classOf[SeatMapController],
    classOf[SeatController],
    classOf[TransactionController])

  override val host = "localhost:8080"
  override val info = Info(version = "1.0")
  override val basePath = "/"    //the basePath for the API you are exposing
  override val apiDocsPath = "api-docs" //where you want the swagger-json endpoint exposed
  override val externalDocs = Some(new ExternalDocs("Core Docs", "http://acme.com/docs"))
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}