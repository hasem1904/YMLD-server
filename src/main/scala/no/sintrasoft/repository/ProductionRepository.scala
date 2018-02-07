package no.sintrasoft.repository

import no.sintrasoft.domain.ProductionRow
import slick.lifted.TableQuery

import no.sintrasoft.domain.Tables.ProductionTable

/**
  * The ProductionRepository provides CRUD operations on the ProductionTable.
  */
class ProductionRepository extends BaseRepository[ProductionTable, ProductionRow](TableQuery[ProductionTable])