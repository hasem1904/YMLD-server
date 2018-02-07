package no.sintrasoft.service

import com.google.inject.Inject
import no.sintrasoft.domain.ProductionRow
import no.sintrasoft.logger.Logger
import no.sintrasoft.repository.ProductionRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ProductionService {
  def getById(id: Long): Future[Option[ProductionRow]]
  def getAll: Future[Seq[ProductionRow]]
  def save(row: ProductionRow): Future[ProductionRow]
  def deleteById(id: Long): Future[Unit]
  def updateById(id: Long, row: ProductionRow): Future[ProductionRow]
}

class ProductionServiceImpl @Inject()(repository: ProductionRepository) extends ProductionService with Logger {
  override def getById(id: Long): Future[Option[ProductionRow]] = {
    val productionRow = repository.getById(id)
    productionRow map { pp =>
      println(s"ProductionRow details: $pp")
    }
    productionRow
  }

  override def getAll: Future[Seq[ProductionRow]] = {
    val allProductionRows = repository.getAll.recover {
      case ex => println("Getting all ProductionRow failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
    allProductionRows
  }

  override def save(row: ProductionRow): Future[ProductionRow] = {
    val savedStatus = repository.save(row)
    savedStatus flatMap  { id =>
      println("ProductionRow Successfully Saved With ID : " + id)
      getById(id).map(_.get)
    } recover {
      case ex => println("ProductionRow save failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
  }

  override def deleteById(id: Long): Future[Unit] = {
    val deleteStatus = repository.deleteById(id)
    deleteStatus map { _ =>
      println(s"ProductionRow with ID $id deleted successfully")
    } recover {
      case ex => println("Delete operation failed.")
        ex.printStackTrace()
    }
  }

  override def updateById(id: Long, row: ProductionRow): Future[ProductionRow] = {
    val updateStatus = repository.updateById(id, row)
    updateStatus map { _ =>
      println(s"Updated the ProductionRow with id '$id' successfully")
    } recover {
      case ex => println("Update operation failed.")
        ex.printStackTrace()
        throw new Exception("Update operation failed")
    }
    getById(row.id).map(_.get)
  }
}
