package no.sintrasoft.controller

import javax.ws.rs.{Path, PathParam}

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.google.inject.{Inject, Singleton}
import io.swagger.annotations._
import no.sintrasoft.domain.{ProductionRequest, ProductionRow}
import no.sintrasoft.service.ProductionServiceImpl
import org.json4s.native.Serialization.writePretty

import scala.concurrent.{ExecutionContext, Future}

/**
  * The ProductionController provides REST services related to the production service.
  *
  * @param service the production service.
  * @param ec      execution context.
  */
@Singleton
@Path("/production")
@Api(value = "/production")
class ProductionController @Inject()(service: ProductionServiceImpl, implicit val ec: ExecutionContext) extends BaseController {

  @Path("/{id}")
  @ApiOperation(httpMethod = "GET", response = classOf[ProductionRow], value = "getProduction")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "Returns a production row by id.")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving productionRow row")))
  def getProduction(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.getById(id).map { pr =>
      HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "DELETE", value = "deleteProduction")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "id", required = true, dataType = "long", value = "Deletes a ProductionRow with the specified id.")))
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error deleting productionRow row")))
  def deleteProduction(@PathParam("id") id: Long): Future[HttpResponse] = {
    service.deleteById(id).map(_ => HttpResponse(StatusCodes.NoContent))
  }

  @ApiOperation(httpMethod = "POST", response = classOf[no.sintrasoft.domain.ProductionRequest], value = "createProduction")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.ProductionRequest", value = "The request resource to post a new production row")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def createProduction(request: ProductionRequest): Future[HttpResponse] = {
    service.save(ProductionRow(id = 1, productionTitle = request.productionTitle, productionStart = request.productionStart, productionEnd = request.productionEnd)).map {
      pr => HttpResponse(StatusCodes.Created, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/{id}")
  @ApiOperation(httpMethod = "PUT", response = classOf[no.sintrasoft.domain.ProductionRequest], value = "updateProduction")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "request", required = true, dataType = "no.sintrasoft.domain.ProductionRequest", value = "updateProduction")))
  @ApiResponses(Array(new ApiResponse(code = 404, message = "Resource not found")))
  def updateProduction(@PathParam("id") id: Long, request: ProductionRequest): Future[HttpResponse] = {
    service.updateById(id, ProductionRow(id = id, productionTitle = request.productionTitle, productionStart = request.productionStart, productionEnd = request.productionEnd)).map {
      pr => HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(pr)))
    }
  }

  @Path("/all")
  @ApiOperation(httpMethod = "GET", response = classOf[ProductionRow], value = "getAllProductions")
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Error retrieving all production row(s)")))
  def getAllProductions: Future[HttpResponse] = {
    service.getAll.flatMap { spr => Future(HttpResponse(StatusCodes.OK, entity = createJsonResponseEntity(writePretty(spr))))
    }
  }
}
