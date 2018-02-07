package no.sintrasoft.controller

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import no.sintrasoft.akka.http.serialization.JsonFormats
import no.sintrasoft.logger.Logger
import org.json4s.{Formats, NoTypeHints, native}

trait BaseController extends Logger with JsonFormats{
  //json4s writes.
  implicit val formats: Formats = native.Serialization.formats(NoTypeHints) + DateSerializer + TimeSerializer

  /**
    * createJsonResponseEntity creates a json HttpEntity.
    *
    * @param json the json string in the response entity
    * @return a HttpEntity with content type `application/json`.
    */
  def createJsonResponseEntity(json: String) = {
    HttpEntity(ContentTypes.`application/json`, json)
  }
}
