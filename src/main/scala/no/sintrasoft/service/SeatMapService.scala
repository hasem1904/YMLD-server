package no.sintrasoft.service

import com.google.inject.Inject
import no.sintrasoft.domain.SeatmapRow
import no.sintrasoft.logger.Logger
import no.sintrasoft.repository.SeatmapRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait SeatMapService {
  def getById(id: Long): Future[Option[SeatmapRow]]
  def getAll: Future[Seq[SeatmapRow]]
  def save(row: SeatmapRow): Future[SeatmapRow]
  def deleteById(id: Long): Future[Unit]
  def updateById(id: Long, row: SeatmapRow): Future[SeatmapRow]
}

class SeatMapServiceImpl @Inject()(repository: SeatmapRepository) extends SeatMapService with Logger{
  override def getById(id: Long): Future[Option[SeatmapRow]] = {
    val seatmapRow = repository.getById(id)
    seatmapRow map { pp =>
      println(s"SeatmapRow details: $pp")
    }
    seatmapRow
  }

  override def getAll: Future[Seq[SeatmapRow]] = {
    val allSeatmapRows = repository.getAll.recover {
      case ex => println("Getting all SeatmapRow failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
    allSeatmapRows
  }

  override def save(row: SeatmapRow): Future[SeatmapRow] = {
    val savedStatus = repository.save(row)
    savedStatus flatMap { id =>
      println("SeatmapRow Successfully Saved With ID : " + id)
      getById(id).map(_.get)
    } recover {
      case ex => println("SeatmapRow save failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
  }

  override def deleteById(id: Long): Future[Unit] = {
    val deleteStatus = repository.deleteById(id)
    deleteStatus map { _ =>
      println(s"SeatmapRow with ID $id deleted successfully")
    } recover {
      case ex => println("Delete operation failed.")
        ex.printStackTrace()
    }
  }

  override def updateById(id: Long, row: SeatmapRow): Future[SeatmapRow] = {
    val updateStatus = repository.updateById(id, row)
    updateStatus map { _ =>
      println(s"Updated the SeatmapRow with id '${row.id}' successfully")
    } recover {
      case ex => println("Update operation failed.")
        ex.printStackTrace()
    }
    getById(row.id).map(_.get)
  }
}
