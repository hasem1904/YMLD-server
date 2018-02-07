package no.sintrasoft.controller

import javax.ws.rs.{Path, PathParam}

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.google.inject.{Inject, Singleton}
import io.swagger.annotations._
import no.sintrasoft.domain.{SeatMapRequest, SeatmapRow}
import no.sintrasoft.service.SeatMapServiceImpl
import org.json4s.native.Serialization.writePretty

import scala.concurrent.{ExecutionContext, Future}


@Singleton
@Path("/seatmap")
@Api(value = "/seatmap")
class SeatMapController @Inject()(service: SeatMapServiceImpl, implicit val ec: ExecutionContext) extends BaseController {

  @Path("/{id}")
  @ApiOperation(httpMethod = "GET", response = classOf[SeatmapRow], value = "getSeatMap")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "The id of the seatMap to lookup")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving SeatMap row")))
  def getSeatMap(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.getById(id).map { sr =>
      HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(sr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "DELETE", value = "deleteSeatMap")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "Deletes a seatMap with the specified id.")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error deleting seatMap row")))
  def deleteSeatMap(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.deleteById(id).map(_ => HttpResponse(StatusCodes.NoContent))
  }

  @ApiOperation(httpMethod = "POST", response = classOf[no.sintrasoft.domain.ProductionRequest], value = "createSeatMap")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.SeatMapRequest", value = "The request resource to post a new seatMap row")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def createSeatMap(request: SeatMapRequest): Future[HttpResponse] = {
    service.save(SeatmapRow(id = 1, seatView = request.seatView)).map {
      sr => HttpResponse(StatusCodes.Created, entity = createJsonResponseEntity(writePretty(sr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "PUT", response = classOf[no.sintrasoft.domain.ProductionRequest], value = "updateSeatMap")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.SeatMapRequest", value = "updateSeatMap")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def updateSeatMap(@PathParam("id") id: Long, request: SeatMapRequest): Future[HttpResponse] = {
    service.updateById(id, SeatmapRow(id = id, seatView = request.seatView)).map {
      sr => HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(sr)))
    }
  }

  @Path("/all")
  @ApiOperation(httpMethod = "GET", response = classOf[SeatmapRow], value = "getAllSeatMaps")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "The id of the seatMap to lookup")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving all seatMap row(s)")))
  def getAllSeatMaps: Future[HttpResponse] = {
    service.getAll.flatMap { spr => Future(HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(spr))))
    }
  }
}
