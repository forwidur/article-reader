package co.kernelnetworks.medstream.server.db

import co.kernelnetworks.medstream.server._
import co.kernelnetworks.medstream.server.serialization.JsonMarshallers
import com.typesafe.scalalogging.slf4j.LazyLogging
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.postgresql.ds.PGSimpleDataSource
import scalikejdbc.{ConnectionPool, DataSourceConnectionPool}

trait DBCore extends JsonMarshallers { this : LazyLogging =>

  private val postgresContainerAlias = getStringDockerVariable("POSTGRES_CONTAINER_ALIAS", "localhost")
  private val postgresDbPort = getIntDockerVariable("POSTGRES_DB_PORT", 5432)
  private val postgresDbName = getStringDockerVariable("POSTGRES_DB_NAME", "casedb")
  private val postgresUser = getStringDockerVariable("POSTGRES_USER", "caseuser")
  private val postgresPassword = getStringDockerVariable("POSTGRES_PASSWORD", "casepwd")

  logger.info(s"Connecting to Postgres DB at $postgresContainerAlias:$postgresDbPort")
  protected def init() = {
    val config = new HikariConfig()
    val pg = new PGSimpleDataSource()
    pg.setServerName(postgresContainerAlias)
    pg.setPortNumber(postgresDbPort)
    pg.setDatabaseName(postgresDbName)
    pg.setUser(postgresUser)
    pg.setPassword(postgresPassword)
    config.setDataSource(pg)
    config.setMaximumPoolSize(10)
    val ds = new HikariDataSource(config)

    ConnectionPool.singleton(new DataSourceConnectionPool(ds))
  }
}

object DBManager extends LazyLogging with DBCore with ScalikeTables {
  init()
}
