package no.sintrasoft.domain

//import slick.lifted.{Rep, Tag}
//import slick.model.Table

import slick.jdbc.DerbyProfile.api._
import scala.reflect._
/**
  * http://reactore.com/repository-patterngeneric-dao-implementation-in-scala-using-slick-3/
  */
trait BaseEntity {
  val id: Long
  //val isDeleted: Boolean
}

abstract class BaseTable[E: ClassTag](tag: Tag, schemaName: Option[String], tableName: String) extends Table[E](tag, schemaName, tableName) {
  val classOfEntity = classTag[E].runtimeClass
  val id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  // val isDeleted: Rep[Boolean] = column[Boolean]("IsDeleted", O.Default(false))
}