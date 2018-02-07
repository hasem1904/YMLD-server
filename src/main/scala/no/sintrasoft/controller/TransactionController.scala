package no.sintrasoft.controller

import javax.ws.rs.Path

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.google.inject.{Inject, Singleton}
import io.swagger.annotations._
import no.sintrasoft.domain.TransactionRow
import no.sintrasoft.service.TransactionServiceImpl
import org.json4s.native.Serialization.writePretty

import scala.concurrent.{ExecutionContext, Future}


@Singleton
@Path("/transactionlog")
@Api(value = "/transactionlog")
class TransactionController @Inject()(service: TransactionServiceImpl, implicit val ec: ExecutionContext) extends BaseController  {
  @Path("/all")
  @ApiOperation(httpMethod = "GET", response = classOf[TransactionRow], value = "getAllTransactionLogs")
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving all transactionLog row(s)")))
  def getAllTransactionLogs: Future[HttpResponse] = {
    service.getAll.flatMap { spr => Future(HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(spr))))
    }
  }
}
