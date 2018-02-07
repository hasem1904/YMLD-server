package no.sintrasoft.akka.http.serialization

import _root_.akka.http.scaladsl.marshalling.{Marshaller, _}
import _root_.akka.http.scaladsl.model._
import _root_.akka.http.scaladsl.unmarshalling.{Unmarshaller, _}
import com.fasterxml.jackson.databind.{DeserializationFeature, SerializationFeature}
import org.json4s.jackson.Serialization
import org.json4s.{Formats, NoTypeHints, native}

/**
  * Implicit marshaling using Jackson and Json4s to replace the built in akka-http/Spray marshaling
  *
  * see:
  * https://github.com/Chumper/addda/blob/master/addda-common/src/main/scala/addda/serialization/JsonSerialization.scala
  * http://queirozf.com/entries/json4s-examples-common-basic-operations-using-jackson-as-backend
  */
trait JsonSerialization extends JsonFormats{
  //val jsonMediaType = MediaTypes.`application/json`
  //val jsonContentType = ContentTypes.`application/json`

  val jsonMediaType: MediaType.WithOpenCharset = MediaType.applicationWithOpenCharset("json")
  val jsonContentType: MediaType.WithOpenCharset = MediaType.applicationWithOpenCharset("json")

  protected val jsonSerialization: Serialization.type = Serialization

  implicit def jsonFormats: Formats = native.Serialization.formats(NoTypeHints) + DateSerializer + TimeSerializer
  //DefaultFormats ++ JavaTypesSerializers.all

  /**
    * marshall AnyRef, that does not have a more specialized marshaller defined, as content type
    * 'application/json' and with a json serializer
    */
  protected implicit def jsonMarshallerAnyRef[A <: AnyRef]: ToEntityMarshaller[A] =
    Marshaller.withOpenCharset(jsonMediaType) { (anyRef, negotiatedCharSet) =>
      HttpEntity(ContentType(jsonMediaType, negotiatedCharSet), jsonSerialization.write(anyRef))
    }

  /**
    * marshal AnyVal, that does not have a more specialized marshaller defined,
    * as content type 'application/json'
    */
  protected implicit def jsonMarshallerAnyVal[A <: AnyVal]: ToEntityMarshaller[A] =
    Marshaller.withOpenCharset(jsonMediaType) { (anyVal, negotiatedCharSet) =>
      HttpEntity(ContentType(jsonMediaType, negotiatedCharSet), anyVal.toString)
    }

  /**
    * unmarshall content type application/json with a json deserializer
    */
  protected implicit def jsonUnmarshaller[A: Manifest]/*(implicit mat: Materializer)*/: FromEntityUnmarshaller[A] =
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(jsonMediaType)
      .mapWithCharset { (data, charset) =>
        val input = if (charset == HttpCharsets.`UTF-8`) data.utf8String else data.decodeString(charset.nioCharset.name)
        jsonSerialization.read(input)
      }

  org.json4s.jackson.JsonMethods.mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
   /* .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)*/

    .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
    .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
    .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
    .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
    .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
}