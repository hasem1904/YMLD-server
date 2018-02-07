package no.sintrasoft.akka.http.serialization

import java.sql.{Date, Time}

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

/**
  * https://stackoverflow.com/questions/27086688/json4s-trouble-while-trying-to-convert-json-attribute-to-java-sql-date
  * https://github.com/json4s/json4s/issues/173
  * https://recalll.co/app/?q=scala%20-%20Json4s:%20Trouble%20while%20trying%20to%20convert%20Json%20attribute%20to%20java.sql.Date
  * https://nmatpt.com/blog/2017/01/29/json4s-custom-serializer/
  */
trait JsonFormats {
  //java.sql.Date => Json string
  case object DateSerializer extends CustomSerializer[java.sql.Date](format => ( {
    case JString(s) => Date.valueOf(s)
    case JNull => null
  }, {
    case d: Date => JString(d.toString)
  }))

  //java.sql.Time => Json string
  case object TimeSerializer extends CustomSerializer[java.sql.Time](format => ( {
    case JString(s) => Time.valueOf(s)
    case JNull => null
  }, {
    case t: Time => JString(t.toString)
  }))
}