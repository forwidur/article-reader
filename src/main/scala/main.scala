import akka.actor.ActorSystem
import co.kernelnetworks.medstream.server.db.{DBManager, ScalikeJdbcHelpers}
import co.kernelnetworks.medstream.server.models.db.Journal
import co.kernelnetworks.medstream.server.serialization.JsonMarshallers
import org.apache.commons.lang3.{StringEscapeUtils, StringUtils}
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

import scala.collection.JavaConversions._

object main extends JsonMarshallers with ScalikeJdbcHelpers {

  implicit val system = ActorSystem("root")
  implicit val executor = system.dispatcher

  def main(args: Array[String]) {

    val processor = new ArticleProcessor()

    processor.parseKeywords(DBManager.getAllKeywords)

    val journals: Seq[Journal] = DBManager.getJournals
    processor.parseJournals(journals.map { v =>
      new JournalJ(v.id.toHexString, v.title, v.publisherName, v.rawTopics.getOrElse(Seq()) : Seq[String])
    })

    for ((start, end) <- DBManager.getSitemapChunks;
         article <- DBManager.getArticlesByIds(DBManager.getArticleIds(start, end))) {

      val body = article.externalProps.rawData.map(raw => StringUtils.normalizeSpace(StringEscapeUtils.unescapeHtml4(Jsoup.clean(raw, Whitelist.none())))).getOrElse("")

      val ja = new ArticleJ(
        article.id.toHexString,
        article.title,
        article.journalId.toHexString,
        article.keywords.getOrElse(Nil) : Seq[String],
        article.externalProps.topics.getOrElse(Nil) : Seq[String],
        article.externalProps.`type`.getOrElse(Nil) : Seq[String],
        body
      )
      processor.parseArticle(ja)
    }
  }
}
