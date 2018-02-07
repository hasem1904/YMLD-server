package no.sintrasoft.repository

import no.sintrasoft.domain.Tables.TransactionTable
import no.sintrasoft.domain.TransactionRow
import slick.lifted.TableQuery

/**
  * The TransactionRepository provides CRUD operations on the TransactionTable.
  */
class TransactionRepository extends BaseRepository[TransactionTable, TransactionRow](TableQuery[TransactionTable])
