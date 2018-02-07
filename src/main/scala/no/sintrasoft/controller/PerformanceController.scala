package no.sintrasoft.controller

import javax.ws.rs.{Path, PathParam}

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.google.inject.{Inject, Singleton}
import io.swagger.annotations._
import no.sintrasoft.domain.{PerformanceRequest, PerformanceRow}
import no.sintrasoft.service.PerformanceServiceImpl
import org.json4s.native.Serialization.writePretty

import scala.concurrent.{ExecutionContext, Future}

@Singleton
@Path("/performance")
@Api(value = "/performance")
class PerformanceController @Inject()(service: PerformanceServiceImpl, implicit val ec: ExecutionContext) extends BaseController {

  @Path("/{id}")
  @ApiOperation(httpMethod = "GET", response = classOf[PerformanceRow], value = "getPerformance")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "The id of the PerformanceRow to lookup")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving performance row")))
  def getPerformance(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.getById(id).map { pr => HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "DELETE", value = "deleteProduction")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "deletePerformance")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error deleting productionRow row")))
  def deletePerformance(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.deleteById(id).map(_ => HttpResponse(StatusCodes.NoContent))
  }

  @ApiOperation(httpMethod = "POST", response = classOf[no.sintrasoft.domain.ProductionRequest], value = "createPerformance")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.PerformanceRow", value = "The request resource to post a new performance row")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def createPerformance(request: PerformanceRequest): Future[HttpResponse] = {
    service.save(PerformanceRow(id = 1, productionNo = request.productionNo, performanceTime = request.performanceTime, performanceDate = request.performanceDate, performanceSeats = request.performanceSeats)).map {
      pr => HttpResponse(StatusCodes.Created, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "PUT", response = classOf[no.sintrasoft.domain.ProductionRequest], value = "updatePerformance")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.ProductionRequest", value = "updateProduction")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def updatePerformance(@PathParam("id") id: Long, request: PerformanceRequest): Future[HttpResponse] = {
    service.updateById(id, PerformanceRow(id = id, request.productionNo, performanceTime = request.performanceTime, performanceDate = request.performanceDate, performanceSeats = request.performanceSeats)).map {
      pr => HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/all")
  @ApiOperation(httpMethod = "GET", response = classOf[PerformanceRow], value = "getAllPerformances")
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving all performance row(s)")))
  def getAllPerformances: Future[HttpResponse] = {
    service.getAll.flatMap { spr => Future(HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(spr))))
    }
  }
}