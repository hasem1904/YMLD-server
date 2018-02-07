package no.sintrasoft.repository

import no.sintrasoft.domain.PerformanceRow
import no.sintrasoft.domain.Tables.PerformanceTable
import slick.lifted.TableQuery

/**
  * The PerformanceRepository provides CRUD operations on the PerformanceTable.
  */
class PerformanceRepository extends BaseRepository[PerformanceTable, PerformanceRow](TableQuery[PerformanceTable])
