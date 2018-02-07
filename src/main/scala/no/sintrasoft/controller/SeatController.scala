package no.sintrasoft.controller

import javax.ws.rs.{Path, PathParam}

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.google.inject.{Inject, Singleton}
import io.swagger.annotations._
import no.sintrasoft.domain.SeatRow
import no.sintrasoft.service.SeatServiceImpl
import org.json4s.native.Serialization.writePretty

import scala.concurrent.{ExecutionContext, Future}

@Singleton
@Path("/seat")
@Api(value = "/seat")
class SeatController @Inject()(service: SeatServiceImpl, implicit val ec: ExecutionContext) extends BaseController {

  @Path("/{id}/performance/{performanceId}")
  @ApiOperation(httpMethod = "GET", response = classOf[SeatRow], value = "getAllSeatByIdAndPriceplan")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "The id of the seat to lookup"),
    new ApiImplicitParam(name = "performanceId", required = true, dataType = "long", value = "The id of the seat to lookup")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving all seats) by id and priceplan")))
  def getAllSeatByIdAndPriceplan(@PathParam("id") id: Long, @PathParam("performanceId") performanceId: Long): Future[HttpResponse] = {
    service.getByIdAndPriceplanNo(id, performanceId).map {
      sr => HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(sr)))
    }
  }

  /*@Path("/{id}")
  @ApiOperation(httpMethod = "DELETE", value = "deleteSeat")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "Deletes a seat with the specified id.")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error deleting productionRow row")))
  def deleteSeat(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.deleteById(id).map(_ => HttpResponse(StatusCodes.NoContent))
  }*/

  /*@ApiOperation(httpMethod = "POST", response = classOf[no.sintrasoft.domain.ProductionRequest], value = "createProduction")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.ProductionRequest", value = "The request resource to post a new production row")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def createSeat(request: ProductionRequest): Future[HttpResponse] = {
    service.save(ProductionRow(id = 1, productionTitle = request.productionTitle, productionStart = request.productionStart, productionEnd = request.productionEnd)).map {
      pr =>
        HttpResponse(StatusCodes.Created, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }*/


  @Path("/all")
  @ApiOperation(httpMethod = "GET", response = classOf[SeatRow], value = "Returns all SeatRow(s).")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "The id of the ProductionRow to lookup")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving all seat row(s)")))
  def getAllSeats: Future[HttpResponse] = {
    service.getAll.flatMap { sr => Future(HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(sr))))
    }
  }
}
