package no.sintrasoft.controller

import javax.ws.rs.{Path, PathParam}

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.google.inject.{Inject, Singleton}
import io.swagger.annotations._
import no.sintrasoft.domain.{PricePlanRequest, PriceplanRow}
import no.sintrasoft.service.PriceplanServiceImpl
import org.json4s.native.Serialization.writePretty

import scala.concurrent.{ExecutionContext, Future}

@Singleton
@Path("/priceplan")
@Api(value = "/priceplan")
class PricePlanController @Inject()(service: PriceplanServiceImpl, implicit val ec: ExecutionContext) extends BaseController {

  @Path("/{id}")
  @ApiOperation(httpMethod = "GET", response = classOf[PriceplanRow], value = "getPricePlan")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "Returns a priceplan row by id.")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving productionRow row")))
  def getPricePlan(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.getById(id).map { pr => HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "DELETE", value = "deletePricePlan")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "Deletes a priceplan with the specified id.")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error deleting priceplan row")))
  def deletePricePlan(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.deleteById(id).map(_ => HttpResponse(StatusCodes.NoContent))
  }

  @ApiOperation(httpMethod = "POST", response = classOf[no.sintrasoft.domain.PriceplanRow], value = "createPricePlan")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.PricePlanRequest", value = "The request resource to post a new priceplan row")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def createPricePlan(request: PricePlanRequest): Future[HttpResponse] = {
    service.save(PriceplanRow(id = 0, priceplanCost = request.priceplanCost)).map {
      pr => HttpResponse(StatusCodes.Created, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "PUT", response = classOf[no.sintrasoft.domain.PriceplanRow], value = "updatePricePlan")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.PricePlanRequest", value = "updatePricePlan")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def updatePricePlan(@PathParam("id") id: Long, request: PricePlanRequest): Future[HttpResponse] = {
    service.updateById(id, PriceplanRow(id = id, priceplanCost = request.priceplanCost)).map {
      pr => HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/all")
  @ApiOperation(httpMethod = "GET", response = classOf[PriceplanRow], value = "getAllPricePlans")
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving all priceplan row(s)")))
  def getAllPricePlans: Future[HttpResponse] = {
    service.getAll.flatMap { spr =>
      Future(HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(spr))))
    }
  }
}
