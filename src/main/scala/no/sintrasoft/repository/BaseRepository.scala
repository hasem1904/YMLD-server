package no.sintrasoft.repository

import com.google.inject.{Guice, Injector}
import com.typesafe.config.Config
import no.sintrasoft.config.Bindings
import no.sintrasoft.domain.Tables.profile
import no.sintrasoft.domain.Tables.profile.api._
import no.sintrasoft.domain.{BaseEntity, BaseTable}
import slick.dbio.Effect
import slick.jdbc.DerbyProfile
import slick.lifted.{CanBeQueryCondition, Rep, TableQuery}
import slick.sql.FixedSqlAction

import scala.concurrent.Future
import scala.reflect._

/**
  * https://github.com/reactore/slick-repository-pattern
  */
object DriverHelper {
  /**
    * Getting instance(s) through Google Guice DI container:
    */
  val injector: Injector = Guice.createInjector(new Bindings)
  val config: Config = injector.getInstance(classOf[Config])
  val db: profile.backend.Database = Database.forConfig("derby", config)

  /* val user = "APP"
   val url = "jdbc:derby://localhost:1527/home/havard/Derby/databases/YMLD;create=true;"
   val password = "APP"
   val jdbcDriver = "org.apache.derby.jdbc.ClientDriver"
   val db: profile.backend.DatabaseDef = Database.forURL(url, user, password, driver = jdbcDriver)*/
}

trait BaseRepositoryComponent[T <: BaseTable[E], E <: BaseEntity] {
  def getById(id: Long): Future[Option[E]]

  def getAll: Future[Seq[E]]

  def filter[C <: Rep[_]](expr: T => C)(implicit wt: CanBeQueryCondition[C]): Future[Seq[E]]

  //def save(row: E): Future[E]
  def save(row: E): Future[Long]

  def deleteById(id: Long): Future[Int]

  def updateById(id: Long, row: E): Future[Int]
}

trait BaseRepositoryQuery[T <: BaseTable[E], E <: BaseEntity] {

  val query: DerbyProfile.api.type#TableQuery[T]

  def getByIdQuery(id: Long) = {
    query.filter(_.id === id)
  }

  def getAllQuery = {
    query
  }

  def filterQuery[C <: Rep[_]](expr: T => C)(implicit wt: CanBeQueryCondition[C]) = {
    query.filter(expr)
  }

  def saveQuery(row: E) = {
    query returning query.map(_.id) += row
  }

  def deleteByIdQuery(id: Long) = {
    query.filter(_.id === id)
  }

  def updateByIdQuery(id: Long, row: E): FixedSqlAction[Int, NoStream, Effect.Write] = {
    //query.filter(_.id === id).insertOrUpdate(row)
    query.filter(_.id === id).update(row)
  }
}

abstract class BaseRepository[T <: BaseTable[E], E <: BaseEntity : ClassTag](clazz: TableQuery[T]) extends BaseRepositoryQuery[T, E] with BaseRepositoryComponent[T, E] {

  val clazzTable: TableQuery[T] = clazz
  lazy val clazzEntity: Class[_] = classTag[E].runtimeClass
  val query: DerbyProfile.api.type#TableQuery[T] = clazz
  val db: DerbyProfile.backend.DatabaseDef = DriverHelper.db

  def getAll: Future[Seq[E]] = {
    db.run(getAllQuery.result)
  }

  def getById(id: Long): Future[Option[E]] = {
    db.run(getByIdQuery(id).result.headOption)
  }

  def filter[C <: Rep[_]](expr: T => C)(implicit wt: CanBeQueryCondition[C]): Future[Seq[E]] = {
    db.run(filterQuery(expr).result)
  }

  def save(row: E): Future[Long] = {
    db.run(saveQuery(row))
  }

  def updateById(id: Long, row: E): Future[Int] = {
    db.run(updateByIdQuery(id, row))
  }

  def deleteById(id: Long): Future[Int] = {
    val q = deleteByIdQuery(id)
    val action = q.delete
    db.run(action)
  }
}