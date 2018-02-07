package no.sintrasoft.service

import com.google.inject.Inject
import no.sintrasoft.domain.PriceplanRow
import no.sintrasoft.logger.Logger
import no.sintrasoft.repository.PriceplanRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PriceplanService {
  def getById(id: Long): Future[Option[PriceplanRow]]

  def getAll: Future[Seq[PriceplanRow]]

  def save(row: PriceplanRow): Future[PriceplanRow]

  def deleteById(id: Long): Future[Unit]

  def updateById(id: Long, row: PriceplanRow): Future[PriceplanRow]
}

class PriceplanServiceImpl @Inject()(repository: PriceplanRepository) extends PriceplanService with Logger {
  override def getById(id: Long): Future[Option[PriceplanRow]] = {
    val priceplanRow = repository.getById(id)
    priceplanRow map { pp =>
      println(s"PriceplanRow details: $pp")
    }
    priceplanRow
  }

  override def getAll: Future[Seq[PriceplanRow]] = {
    val allPriceplanRows = repository.getAll.recover {
      case ex => println("Getting all PriceplanRow failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
    allPriceplanRows
  }

  override def save(row: PriceplanRow): Future[PriceplanRow] = {
    val savedStatus = repository.save(row)
    savedStatus flatMap { id =>
      println("PriceplanRow Successfully Saved With ID : " + id)
      getById(id).map(_.get)
    } recover {
      case ex => println("PriceplanRow save failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
  }

  override def deleteById(id: Long): Future[Unit] = {
    val deleteStatus = repository.deleteById(id)
    deleteStatus map { _ =>
      println(s"PriceplanRow with ID $id deleted successfully")
    } recover {
      case ex => println("Delete operation failed.")
        ex.printStackTrace()
    }
  }

  override def updateById(id: Long, row: PriceplanRow): Future[PriceplanRow] = {
    val updateStatus = repository.updateById(id, row)
    updateStatus map { _ =>
      println(s"Updated the PriceplanRow with id '${row.id}' successfully")
    } recover {
      case ex => println("Update operation failed.")
        ex.printStackTrace()
    }
    getById(row.id).map(_.get)
  }
}
