package no.sintrasoft.service

import com.google.inject.Inject
import no.sintrasoft.domain.PerformanceRow
import no.sintrasoft.logger.Logger
import no.sintrasoft.repository.PerformanceRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PerformanceService {
  def getById(id: Long): Future[Option[PerformanceRow]]

  def getAll: Future[Seq[PerformanceRow]]

  def save(row: PerformanceRow): Future[PerformanceRow]

  def deleteById(id: Long): Future[Unit]

  def updateById(id: Long, row: PerformanceRow): Future[PerformanceRow]

  //def filter[C <: Rep[_]](expr: T => C)(implicit wt: CanBeQueryCondition[C]): Future[Seq[PerformanceRow]]
}

class PerformanceServiceImpl @Inject()(repository: PerformanceRepository) extends PerformanceService with Logger {
  //override def filter[C <: Rep[_]](expr: Any => C)(implicit wt: CanBeQueryCondition[C]) = ???

  override def getById(id: Long): Future[Option[PerformanceRow]] = {
    val performanceRow = repository.getById(id)
    performanceRow map { pr =>
      logger.debug(s"> Debug: ProductionRow details: $pr")
    }
    performanceRow
  }

  override def getAll: Future[Seq[PerformanceRow]] = {
    val allPerformanceRows = repository.getAll.recover {
      case ex => logger.error("Getting all ProductionRow failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
    allPerformanceRows
  }

  override def save(row: PerformanceRow): Future[PerformanceRow] = {
    val savedStatus = repository.save(row)
    savedStatus flatMap { id =>
      logger.debug("ProductionRow Successfully Saved With ID : " + id)
      getById(id).map(_.get)
    } recover {
      case ex => logger.error("ProductionRow save failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
  }

  override def deleteById(id: Long): Future[Unit] = {
    val deleteStatus = repository.deleteById(id)
    deleteStatus map { _ =>
      logger.debug(s"ProductionRow with ID $id deleted successfully")
    } recover {
      case ex => logger.debug("Delete operation failed.")
        ex.printStackTrace()
    }
  }

  override def updateById(id: Long, row: PerformanceRow): Future[PerformanceRow] = {
    val updateStatus = repository.updateById(id, row)
    updateStatus map { _ =>
      logger.debug(s"Updated the ProductionRow with id '${row.id}' successfully")
    } recover {
      case ex => logger.error("Update operation failed.")
        ex.printStackTrace()
    }
    getById(row.id).map(_.get)
  }
}
