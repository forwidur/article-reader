package co.kernelnetworks.medstream.server.db

import co.kernelnetworks.medstream.model._
import co.kernelnetworks.medstream.server.models.db._
import co.kernelnetworks.medstream.server.models.shared.AbstractSection
import org.bson.types.ObjectId
import scalikejdbc._
import shapeless.{Poly1, LabelledGeneric}
import shapeless.record._  // DO NOT REMOVE

import scala.concurrent.{Future, ExecutionContext}

trait ScalikeTables extends ScalikeJdbcHelpers {
  this: DBCore =>

  private object toSqlTypedHlist extends Poly1 {
    implicit def atAnything[T : SqlTyped] = at[T](_.toSqlType)
  }

  implicit val session = AutoSession

  //////////////////////////////// Articles table //////////////////////////////////////////////////////////////////////

  private val articleColumns = Article.column

  implicit def RsToArticle(c : String => String, rs : WrappedResultSet) : Article = {

    val id = rs.oid(c(articleColumns.id))
    val ids = rs.jsonObj[Ids](c(articleColumns.ids))
    val title = rs.string(c(articleColumns.title))
    val journalId = rs.oid(c(articleColumns.journalId))
    val issueId = rs.oidOpt(c(articleColumns.issueId))
    val contributors = rs.array2Opt[Contributor](c(articleColumns.contributors)).map(_.toList)
    val references = rs.array2Opt[Reference](c(articleColumns.references)).map(_.toList)
    val citedBy = rs.array2Opt[Reference](c(articleColumns.citedBy)).map(_.toList)
    val abstrct = rs.array2Opt[(AbstractSection, String)](c(articleColumns.abstrct)).map(_.toList)
    val accessType = ArticleAccessType.fromId(rs.byte(c(articleColumns.accessType)))
    val issuedAt = rs.dateOpt(c(articleColumns.issuedAt)).map(_.toLocalDate)
    val keywords = rs.array2Opt[String](c(articleColumns.keywords))
    val webUrl = rs.string(c(articleColumns.webUrl))
    val providerId = rs.string(c(articleColumns.providerId))
    val ingestionState = IngestionState.fromId(rs.byte(c(articleColumns.ingestionState)))
    val externalProps = rs.jsonObj[ExternalArticleProps](c(articleColumns.externalProps))
    Article(id, ids, title, journalId, issueId, contributors, references, citedBy, abstrct.map(_.toMap), None, accessType, issuedAt, keywords, webUrl, providerId, ingestionState, externalProps)
  }

  def getSitemapChunks: Seq[(ObjectId, ObjectId)] = {

    val a = Article.syntax("a")

    def getNextId(fromInc : ObjectId, offset : Int) : Option[ObjectId] =
      sql"select ${a.result.id} from ${Article.as(a)} where ${a.id} >= ${fromInc.toSqlType} order by ${a.id} asc limit 1 offset ${offset}".map(rs => rs.oid(a.resultName.id)).single.apply()

    val result =
    for (startId <- sql"select ${a.result.id} from ${Article.as(a)} order by ${a.id} asc limit 1".map(rs => rs.oid(a.resultName.id)).single.apply();
         lastId <- sql"select ${a.result.id} from ${Article.as(a)} order by ${a.id} desc limit 1".map(rs => rs.oid(a.resultName.id)).single.apply()
    ) yield {

      def loop(fromInc: ObjectId): Stream[(ObjectId, ObjectId)] =
        getNextId(fromInc, 1000) match {
          case Some(nextId) => (fromInc, nextId) #:: loop(nextId)
          case None => (fromInc, lastId) #:: Stream.empty
        }

      loop(startId).toSeq
    }

    result.getOrElse(Nil)
  }

  def getArticleIds(fromInc: ObjectId, toExcl: ObjectId): Seq[ObjectId] = {

    val a = Article.syntax("a")
    withSQL {
      select(a.result.id)
        .from(Article as a)
        .where
        .ge(a.id, fromInc.toSqlType)
        .and
        .lt(a.id, toExcl.toSqlType)
    }.map(_.oid(a.resultName.id)).list.apply()
  }

  def getArticleById(id: ObjectId)(implicit ec: ExecutionContext): Future[Option[Article]] = {

    val a = Article.syntax("a")

    val result =
    withSQL {
      select
        .from(Article as a)
        .where
        .eq(a.id, id.toSqlType)
    }.map(Article(a)).single.apply()

    Future.successful(result)
  }

  def getArticlesByIds(ids: Seq[ObjectId]): Seq[Article] = {

    val a = Article.syntax("a")

      withSQL {
        select
          .from(Article as a)
          .where
          .in(a.id, ids.map(_.toSqlType))
      }.map(Article(a)).list.apply()
  }

  def getAllKeywords : Seq[String] = {

    val a = Article.syntax("a")
    sql"select DISTINCT unnest(keywords) as kw from ${Article.as(a)}".map(rs => rs.string(1)).list.apply()
  }

  //////////////////////////////// Journals table //////////////////////////////////////////////////////////////////////
  private val journalColumns = Journal.column
  private val genJournal = LabelledGeneric[Journal]

  implicit def RsToJournal(c : String => String, rs : WrappedResultSet) : Journal = {

    val id = rs.oid(c(journalColumns.id))
    val ids = rs.jsonObj[Ids](c(journalColumns.ids))
    val title = rs.string(c(journalColumns.title))
    val publisherName = rs.string(c(journalColumns.publisherName))
    val PMCUrl = rs.stringOpt(c(journalColumns.PMCUrl))
    val rawTopics = rs.array2Opt[String](c(journalColumns.rawTopics))
    val rawData = rs.stringOpt(c(journalColumns.rawData))

    Journal(id, ids, title, publisherName, PMCUrl, rawTopics, rawData)
  }

  def getJournals: Seq[Journal] = {
    val syntax = Journal.syntax("j")
    withSQL {
      select.from(Journal as syntax)
    }.map(Journal(syntax)).list()()
  }
}
