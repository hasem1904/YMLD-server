package no.sintrasoft.domain

/** Entity class storing rows of table Performances
 *
    *  @param id Database column PERFORMANCE_NO SqlType(INTEGER), AutoInc, PrimaryKey
    *  @param productionNo Database column PRODUCTION_NO SqlType(INTEGER)
    *  @param performanceTime Database column PERFORMANCE_TIME SqlType(TIME)
    *  @param performanceDate Database column PERFORMANCE_DATE SqlType(DATE)
    *  @param performanceSeats Database column PERFORMANCE_SEATS SqlType(INTEGER) */
  case class PerformanceRow(id: Long, productionNo: Long, performanceTime: java.sql.Time, performanceDate: java.sql.Date, performanceSeats: Int) extends BaseEntity

  /** Entity class storing rows of table Priceplan
    *  @param id Database column PRICEPLAN_NO SqlType(INTEGER), PrimaryKey
    *  @param priceplanCost Database column PRICEPLAN_COST SqlType(DECIMAL) */
  case class PriceplanRow(id: Long, priceplanCost: Double) extends BaseEntity

  /** Entity class storing rows of table Productions
    *  @param id Database column PRODUCTION_NO SqlType(INTEGER), AutoInc, PrimaryKey
    *  @param productionTitle Database column PRODUCTION_TITLE SqlType(VARCHAR), Length(256,true)
    *  @param productionStart Database column PRODUCTION_START SqlType(DATE)
    *  @param productionEnd Database column PRODUCTION_END SqlType(DATE) */
  case class ProductionRow(id: Long, productionTitle: Option[String], productionStart: Option[java.sql.Date], productionEnd: Option[java.sql.Date]) extends BaseEntity

  /** Entity class storing rows of table Seatmap
    *  @param id Database column SEAT_NO SqlType(INTEGER), PrimaryKey
    *  @param seatView Database column SEAT_VIEW SqlType(BLOB) */
  case class SeatmapRow(id: Long, seatView: Option[java.sql.Blob]) extends BaseEntity

  /** Entity class storing rows of table Seats
    *  @param id Database column PERFORMANCE_NO SqlType(INTEGER)
    *  @param performanceNo Database column SEAT_NO SqlType(INTEGER)
    *  @param priceplanNo Database column PRICEPLAN_NO SqlType(INTEGER)
    *  @param seatAvailable Database column SEAT_AVAILABLE SqlType(INTEGER) */
  case class SeatRow(id: Long, performanceNo: Long, priceplanNo: Long, seatAvailable: Int) extends BaseEntity

  /** Entity class storing rows of table Transactions
    *  @param id Database column TX_NO SqlType(INTEGER), AutoInc, PrimaryKey
    *  @param txType Database column TX_TYPE SqlType(VARCHAR), Length(32,true)
    *  @param txDate Database column TX_DATE SqlType(DATE)
    *  @param performanceNo Database column PERFORMANCE_NO SqlType(INTEGER)
    *  @param seatNo Database column SEAT_NO SqlType(INTEGER) */
  case class TransactionRow(id: Long, txType: String, txDate: java.sql.Date, performanceNo: Long, seatNo: Long) extends BaseEntity


  /** Stand-alone Slick data model for immediate use */
  object Tables extends {
    val profile = slick.jdbc.DerbyProfile
  } with Tables

  trait Tables {
    val profile: slick.jdbc.JdbcProfile

    import profile.api._
    import slick.model.ForeignKeyAction

    val schema = "APP"

    /** Table description of table PERFORMANCES. Objects of this class serve as prototypes for rows in queries. */
    class PerformanceTable(_tableTag: Tag) extends BaseTable[PerformanceRow](_tableTag, Some(schema), "PERFORMANCES") {
      def * = (id, productionNo, performanceTime, performanceDate, performanceSeats) <> (PerformanceRow.tupled, PerformanceRow.unapply)

      /** Maps whole row to an option. Useful for outer joins. */
      def ? = (Rep.Some(id), Rep.Some(productionNo), Rep.Some(performanceTime), Rep.Some(performanceDate), Rep.Some(performanceSeats)).shaped.<>({ r => import r._; _1.map(_ => PerformanceRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

      /** Database column PERFORMANCE_NO SqlType(INTEGER), AutoInc, PrimaryKey */
      override val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
      /** Database column PRODUCTION_NO SqlType(INTEGER) */
      val productionNo: Rep[Long] = column[Long]("PRODUCTION_NO")
      /** Database column PERFORMANCE_TIME SqlType(TIME) */
      val performanceTime: Rep[java.sql.Time] = column[java.sql.Time]("PERFORMANCE_TIME")
      /** Database column PERFORMANCE_DATE SqlType(DATE) */
      val performanceDate: Rep[java.sql.Date] = column[java.sql.Date]("PERFORMANCE_DATE")
      /** Database column PERFORMANCE_SEATS SqlType(INTEGER) */
      val performanceSeats: Rep[Int] = column[Int]("PERFORMANCE_SEATS")

      /** Foreign key referencing Productions (database name SQL171101082727501) */
      lazy val productionsFk = foreignKey("SQL171101082727501", productionNo, productionTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    }
    /** Collection-like TableQuery object for table Performances */
    lazy val performanceTable = new TableQuery(tag => new PerformanceTable(tag))

    /** Table description of table PRICEPLAN. Objects of this class serve as prototypes for rows in queries. */
    class PriceplanTable(_tableTag: Tag) extends BaseTable[PriceplanRow](_tableTag, Some(schema), "PRICEPLAN") {
      def * = (id, priceplanCost) <> (PriceplanRow.tupled, PriceplanRow.unapply)
      /** Maps whole row to an option. Useful for outer joins. */
      def ? = (Rep.Some(id), Rep.Some(priceplanCost)).shaped.<>({ r=>import r._; _1.map(_=> PriceplanRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

      /** Database column PRICEPLAN_NO SqlType(INTEGER), PrimaryKey */
      override val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
      /** Database column PRICEPLAN_COST SqlType(DECIMAL) */
      val priceplanCost: Rep[Double] = column[Double]("PRICEPLAN_COST")
    }
    /** Collection-like TableQuery object for table Priceplan */
    lazy val priceplanTable = new TableQuery(tag => new PriceplanTable(tag))

    /** Table description of table PRODUCTIONS. Objects of this class serve as prototypes for rows in queries. */
    class ProductionTable(_tableTag: Tag) extends BaseTable[ProductionRow](_tableTag, Some(schema), "PRODUCTIONS") {
      def * = (id, productionTitle, productionStart, productionEnd) <> (ProductionRow.tupled, ProductionRow.unapply)
      /** Maps whole row to an option. Useful for outer joins. */
      def ? = (Rep.Some(id), productionTitle, productionStart, productionEnd).shaped.<>({ r=>import r._; _1.map(_=> ProductionRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

      /** Database column PRODUCTION_NO SqlType(INTEGER), AutoInc, PrimaryKey */
      override val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
      /** Database column PRODUCTION_TITLE SqlType(VARCHAR), Length(256,true) */
      val productionTitle: Rep[Option[String]] = column[Option[String]]("PRODUCTION_TITLE", O.Length(256,varying=true))
      /** Database column PRODUCTION_START SqlType(DATE) */
      val productionStart: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("PRODUCTION_START")
      /** Database column PRODUCTION_END SqlType(DATE) */
      val productionEnd: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("PRODUCTION_END")
    }
    /** Collection-like TableQuery object for table Productions */
    lazy val productionTable = new TableQuery(tag => new ProductionTable(tag))

    /** Table description of table SEATMAP. Objects of this class serve as prototypes for rows in queries. */
    class SeatmapTable(_tableTag: Tag) extends BaseTable[SeatmapRow](_tableTag, Some(schema),"SEATMAP") {
      def * = (id, seatView) <> (SeatmapRow.tupled, SeatmapRow.unapply)
      /** Maps whole row to an option. Useful for outer joins. */
      def ? = (Rep.Some(id), seatView).shaped.<>({ r=>import r._; _1.map(_=> SeatmapRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

      /** Database column SEAT_NO SqlType(INTEGER), PrimaryKey */
      override val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
      /** Database column SEAT_VIEW SqlType(BLOB) */
      val seatView: Rep[Option[java.sql.Blob]] = column[Option[java.sql.Blob]]("SEAT_VIEW")
    }
    /** Collection-like TableQuery object for table Seatmap */
    lazy val seatmapTable = new TableQuery(tag => new SeatmapTable(tag))

    /** Table description of table SEATS. Objects of this class serve as prototypes for rows in queries. */
    class SeatTable(_tableTag: Tag) extends BaseTable[SeatRow](_tableTag,Some("APP"), "SEATS") {
      def * = (id, performanceNo, priceplanNo, seatAvailable) <> (SeatRow.tupled, SeatRow.unapply)
      /** Maps whole row to an option. Useful for outer joins. */
      def ? = (Rep.Some(id), Rep.Some(performanceNo), Rep.Some(priceplanNo), Rep.Some(seatAvailable)).shaped.<>({ r=>import r._; _1.map(_=> SeatRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

      /** Database column ID SqlType(INTEGER) */
      override val id: Rep[Long] = column[Long]("ID")
      /** Database column PERFORMANCE_NO SqlType(INTEGER) */
      val performanceNo: Rep[Long] = column[Long]("PERFORMANCE_NO")
      /** Database column PRICEPLAN_NO SqlType(INTEGER) */
      val priceplanNo: Rep[Long] = column[Long]("PRICEPLAN_NO")
      /** Database column SEAT_AVAILABLE SqlType(INTEGER) */
      val seatAvailable: Rep[Int] = column[Int]("SEAT_AVAILABLE")

      /** Primary key of Seats (database name SEATUNIQUE) */
      val pk = primaryKey("SEATUNIQUE", (id, performanceNo))

      /** Foreign key referencing Performances (database name PERFORMANCES) */
      lazy val performancesFk = foreignKey("PERFORMANCES", id, performanceTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
      /** Foreign key referencing Priceplan (database name PRICEPLAN) */
      lazy val priceplanFk = foreignKey("PRICEPLAN", priceplanNo, priceplanTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
      /** Foreign key referencing Seatmap (database name SEATMAP) */
      lazy val seatmapFk = foreignKey("SEATMAP", performanceNo, seatmapTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    }
    /** Collection-like TableQuery object for table Seats */
    lazy val seatTable = new TableQuery(tag => new SeatTable(tag))

    /** Table description of table TRANSACTIONS. Objects of this class serve as prototypes for rows in queries. */
    class TransactionTable(_tableTag: Tag) extends BaseTable[TransactionRow](_tableTag, Some(schema), "TRANSACTIONS") {
      def * = (id, txType, txDate, performanceNo, seatNo) <> (TransactionRow.tupled, TransactionRow.unapply)
      /** Maps whole row to an option. Useful for outer joins. */
      def ? = (Rep.Some(id), Rep.Some(txType), Rep.Some(txDate), Rep.Some(performanceNo), Rep.Some(seatNo)).shaped.<>({ r=>import r._; _1.map(_=> TransactionRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

      /** Database column TX_NO SqlType(INTEGER), AutoInc, PrimaryKey */
      override val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
      /** Database column TX_TYPE SqlType(VARCHAR), Length(32,true) */
      val txType: Rep[String] = column[String]("TX_TYPE", O.Length(32,varying=true))
      /** Database column TX_DATE SqlType(DATE) */
      val txDate: Rep[java.sql.Date] = column[java.sql.Date]("TX_DATE")
      /** Database column PERFORMANCE_NO SqlType(INTEGER) */
      val performanceNo: Rep[Long] = column[Long]("PERFORMANCE_NO")
      /** Database column SEAT_NO SqlType(INTEGER) */
      val seatNo: Rep[Long] = column[Long]("SEAT_NO")

      /** Foreign key referencing Performances (database name SQL171101082727720) */
      lazy val performancesFk = foreignKey("SQL171101082727720", performanceNo, performanceTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
      /** Foreign key referencing Seatmap (database name SQL171101082727721) */
      lazy val seatmapFk = foreignKey("SQL171101082727721", seatNo, seatmapTable)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    }
    /** Collection-like TableQuery object for table Transactions */
    lazy val transactionTable = new TableQuery(tag => new TransactionTable(tag))
}
