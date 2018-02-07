package no.sintrasoft

import _root_.akka.actor.ActorSystem
import _root_.akka.http.scaladsl.Http
import _root_.akka.stream.ActorMaterializer
import com.github.swagger.akka.SwaggerSite
import com.google.inject.name.Named
import com.google.inject.{Guice, Inject, Injector}
import com.typesafe.config.Config
import no.sintrasoft.akka.http.routes.{PerformanceRoutes, _}
import no.sintrasoft.config.Bindings
import no.sintrasoft.domain.ProductionRow
import no.sintrasoft.logger.Logger
import no.sintrasoft.repository.ProductionRepository
import no.sintrasoft.service.{ProductionService, ProductionServiceImpl}
import no.sintrasoft.swagger.SwaggerDocService
import no.sintrasoft.util.Utils

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Random}

class YmldApp @Inject()(@Named("name") name: String, @Named("version") version: String, @Named("environment") environment: String, implicit val ec: ExecutionContext) extends Logger {
  Utils.printBanner(name, version, environment, "2.12.2", "2.4.1")
}

object YmldApp extends App with Logger with SwaggerSite {
  /**
    * Getting instance(s) through Google Guice DI container:
    */
  val injector: Injector = Guice.createInjector(new Bindings)
  implicit val system: ActorSystem = injector.getInstance(classOf[ActorSystem])
  implicit val executionContext: ExecutionContext = injector.getInstance(classOf[ExecutionContext])
  implicit val materializer: ActorMaterializer = injector.getInstance(classOf[ActorMaterializer])
  val config: Config = injector.getInstance(classOf[Config])
  val ymldApp = injector.getInstance(classOf[YmldApp])

  //Akka Http route
  val swaggerRoutes = injector.getInstance(classOf[SwaggerRoutes])
  val performanceRoutes = injector.getInstance(classOf[PerformanceRoutes])
  val pricePlanRoutes = injector.getInstance(classOf[PricePlanRoutes])
  val productionRoutes = injector.getInstance(classOf[ProductionRoutes])
  val seatRoutes = injector.getInstance(classOf[SeatRoutes])
  val seatmapRoutes = injector.getInstance(classOf[SeatMapRoutes])
  val transactionRoutes = injector.getInstance(classOf[TransactionRoutes])

  //Composing Akka Http routes
  val route = {
    SwaggerDocService.routes ~
      swaggerSiteRoute ~
      swaggerRoutes.apply() ~
      performanceRoutes.apply() ~
      pricePlanRoutes.apply() ~
      productionRoutes.apply() ~
      seatRoutes.apply() ~
      seatmapRoutes.apply() ~
      transactionRoutes.apply()
  }


  //Akka Http Server
  val serverBindingFuture = Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))
  serverBindingFuture.onComplete {
    case Failure(t) => println(s"> Server binding failed with ${t.getMessage}")
      system.terminate()
    case scala.util.Success(s) => println(s"> Server started at: ${s.localAddress.getHostName} ${s.localAddress.getPort}...")
  }
}

/**
  * Testing repository/service methods
  */
object ProductOperations extends Logger {
  val prodRepo = new ProductionRepository

  /**
    * Getting instance(s) through Google Guice DI container:
    */
  val injector: Injector = Guice.createInjector(new Bindings)
  val productionService: ProductionService = injector.getInstance(classOf[ProductionServiceImpl])
  val actorSystem: ActorSystem = injector.getInstance(classOf[ActorSystem])
  implicit val ec: ExecutionContext = injector.getInstance(classOf[ExecutionContext])

  //val db: profile.backend.Database = profile.backend.Database.forConfig("db")

  def saveToDB(pr: ProductionRow): Future[ProductionRow] = {
    println("-------------")
    println("Saving to DB:")
    println("-------------")
    productionService.save(pr)
  }

  def getAllFromDB: Future[Seq[ProductionRow]] = {
    println("\n-----------------------------------------------")
    println("Getting all the ProductionRow from the database")
    println("-----------------------------------------------")
    val allUsers = productionService.getAll
    allUsers map { p =>
      println(s"There are ${p.size} ProductionRow in the database")
      p.foreach(println)
    }
    allUsers
  }

  def getProductionRowById(id: Long): Future[Option[ProductionRow]] = {
    println("\n---------------------------------------")
    println(s"Getting the ProductionRow with id: $id")
    println("---------------------------------------")
    productionService.getById(id)
  }

  def updateProductionRow(id: Long, pr: ProductionRow): Future[ProductionRow] = {
    println("\n---------------------------------------")
    println(s"Updating the ProductionRow with id $id")
    println("---------------------------------------")
    val modifiedUser: ProductionRow = pr.copy(productionTitle = Option(s"The new production - ${Random.nextInt(100)}"))
    productionService.updateById(id, modifiedUser)
  }

  def deleteProductionRow(id: Long): Future[Unit] = {
    println("\n---------------------------------------")
    println(s"Deleting the ProductionRow with id $id")
    println("---------------------------------------")
    productionService.deleteById(id)
  }

  /**
    * Shuts down the application and the Akka system.
    */
  def shutdown(): Unit = {
    println("\n> Ymld App done. Shutting down the program.")
    //Shutting down the akka system.
    val shutdown = actorSystem.terminate()
    Await.result(shutdown, 1.minute)
    System.exit(-1)
  }
}
