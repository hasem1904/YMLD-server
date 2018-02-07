package no.sintrasoft.service

import com.google.inject.Inject
import no.sintrasoft.domain.TransactionRow
import no.sintrasoft.logger.Logger
import no.sintrasoft.repository.TransactionRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait TransactionService {
  def getById(id: Long): Future[Option[TransactionRow]]
  def getAll: Future[Seq[TransactionRow]]
  def save(row: TransactionRow): Future[TransactionRow]
  def deleteById(id: Long): Future[Unit]
  def updateById(id: Long, row: TransactionRow): Future[TransactionRow]
}

class TransactionServiceImpl @Inject()(repository: TransactionRepository) extends TransactionService with Logger {
  override def getById(id: Long): Future[Option[TransactionRow]] = {
    val transactionRow = repository.getById(id)
    transactionRow map { pp =>
      println(s"TransactionRow details: $pp")
    }
    transactionRow
  }

  override def getAll: Future[Seq[TransactionRow]] = {
    val allTransactionRows = repository.getAll.recover {
      case ex => println("Getting all TransactionRow failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
    allTransactionRows
  }

  override def save(row: TransactionRow): Future[TransactionRow] = {
    val savedStatus = repository.save(row)
    savedStatus flatMap { id =>
      println("TransactionRow Successfully Saved With ID : " + id)
      getById(id).map(_.get)
    } recover {
      case ex => println("TransactionRow save failed. Check stacktrace for more details.")
        ex.printStackTrace()
        throw new Exception("Save operation failed")
    }
  }

  override def deleteById(id: Long): Future[Unit] = {
    val deleteStatus = repository.deleteById(id)
    deleteStatus map { _ =>
      println(s"TransactionRow with ID $id deleted successfully")
    } recover {
      case ex => println("Delete operation failed.")
        ex.printStackTrace()
    }
  }

  override def updateById(id: Long, row: TransactionRow): Future[TransactionRow] = {
    val updateStatus = repository.updateById(id, row)
    updateStatus map { _ =>
      println(s"Updated the TransactionRow with id '${row.id}' successfully")
    } recover {
      case ex => println("Update operation failed.")
        ex.printStackTrace()
    }
    getById(row.id).map(_.get)
  }
}
