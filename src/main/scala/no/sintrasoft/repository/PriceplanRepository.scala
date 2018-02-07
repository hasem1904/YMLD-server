package no.sintrasoft.repository

import no.sintrasoft.domain.PriceplanRow
import no.sintrasoft.domain.Tables.PriceplanTable
import slick.lifted.TableQuery

/**
  * The PriceplanRepository provides CRUD operations on the PriceplanTable.
  */
class PriceplanRepository extends BaseRepository[PriceplanTable, PriceplanRow](TableQuery[PriceplanTable])