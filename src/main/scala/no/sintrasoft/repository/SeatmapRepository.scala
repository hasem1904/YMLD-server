package no.sintrasoft.repository

import no.sintrasoft.domain.SeatmapRow
import no.sintrasoft.domain.Tables.SeatmapTable
import slick.lifted.TableQuery

/**
  * The SeatmapRepository provides CRUD operations on the SeatmapTable.
  */
class SeatmapRepository extends BaseRepository[SeatmapTable, SeatmapRow](TableQuery[SeatmapTable])
