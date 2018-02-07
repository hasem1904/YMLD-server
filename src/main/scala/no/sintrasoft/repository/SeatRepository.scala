package no.sintrasoft.repository

import no.sintrasoft.domain.{SeatRow, Tables}
import no.sintrasoft.domain.Tables.SeatTable
import slick.jdbc.DerbyProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

/**
  * The SeatRepository provides CRUD operations on the SeatTable.
  */
class SeatRepository extends BaseRepository[SeatTable, SeatRow](TableQuery[SeatTable]){

  def getByIdAndPerformanceNoQuery(id: Long, performanceNo:Long): Query[Tables.SeatTable, SeatRow, Seq] = {
    query.filter(sr => sr.id === id && sr.performanceNo === performanceNo)
  }

  def getByIdAndPerformanceNo(id: Long, performanceNo:Long): Future[Seq[SeatRow]] = {
    db.run(getByIdAndPerformanceNoQuery(id, performanceNo).result)
  }
}
