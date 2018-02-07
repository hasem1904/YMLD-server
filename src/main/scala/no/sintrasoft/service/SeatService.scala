package no.sintrasoft.service

import com.google.inject.Inject
import no.sintrasoft.domain.SeatRow
import no.sintrasoft.logger.Logger
import no.sintrasoft.repository.SeatRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait SeatService {
  def getById(id: Long): Future[Option[SeatRow]]
  def getAll: Future[Seq[SeatRow]]
  def save(row: SeatRow): Future[SeatRow]
  def deleteById(id: Long): Future[Unit]
  def updateById(id: Long, row: SeatRow): Future[SeatRow]
  def getByIdAndPriceplanNo(id: Long, priceplan:Long): Future[Seq[SeatRow]]
}

class SeatServiceImpl @Inject()(repository: SeatRepository) extends SeatService with Logger {
  override def getById(id: Long): Future[Option[SeatRow]] = {
    val seatRow = repository.getById(id)
    seatRow map { sr =>
      println(s"SeatRow details: $sr")
    }
    seatRow
  }

  def getByIdAndPriceplanNo(id: Long, performance:Long): Future[Seq[SeatRow]]= {
    val seatRow = repository.getByIdAndPerformanceNo(id, performance)
    seatRow map { sr =>
      println(s"SeatRow details: ${sr.toList.toString()}")
    }
    seatRow
  }

  override def getAll: Future[Seq[SeatRow]] = {
    val allSeatRows = repository.getAll.recover {
      case ex => println("Getting all allSeatRows failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
    allSeatRows
  }

  override def save(row: SeatRow): Future[SeatRow] = {
    val savedStatus = repository.save(row)
    savedStatus flatMap  { id =>
      println("SeatRow Successfully Saved With ID : " + id)
      getById(id).map(_.get)
    } recover {
      case ex => println("SeatRow save failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
  }

  override def deleteById(id: Long): Future[Unit] = {
    val deleteStatus = repository.deleteById(id)
    deleteStatus map { _ =>
      println(s"SeatRow with ID $id deleted successfully")
    } recover {
      case ex => println("Delete operation failed.")
        ex.printStackTrace()
    }
  }

  override def updateById(id: Long, row: SeatRow): Future[SeatRow] = {
    val updateStatus = repository.updateById(id, row)
    updateStatus map { _ =>
      println(s"Updated the SeatRow with id '${row.id}' successfully")
    } recover {
      case ex => println("Update operation failed.")
        ex.printStackTrace()
    }
    getById(row.id).map(_.get)
  }
}
