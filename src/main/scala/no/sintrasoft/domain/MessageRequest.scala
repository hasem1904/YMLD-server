package no.sintrasoft.domain

/**
  *
  * @param message
  */
sealed case class MessageRequest(message: String)
sealed case class ProductionRequest(productionTitle: Option[String], productionStart: Option[java.sql.Date], productionEnd: Option[java.sql.Date])
sealed case class SeatMapRequest(seatView: Option[java.sql.Blob])
sealed case class SeatRequest(performanceNo: Long, priceplanNo: Long, seatAvailable: Int)
sealed case class PerformanceRequest(productionNo: Long, performanceTime: java.sql.Time, performanceDate: java.sql.Date, performanceSeats: Int)
sealed case class PricePlanRequest(priceplanCost: Double)
