package co.kernelnetworks.medstream.server.models.db

import java.time.LocalDate

import co.kernelnetworks.medstream.model.{ArticleAccessType, Contributor, Reference}
import co.kernelnetworks.medstream.server.db.SQLSupportViaImplicit
import co.kernelnetworks.medstream.server.models.shared.AbstractSection
import org.bson.types.ObjectId

final case class Article(id: ObjectId,
                         ids: Ids,
                         title: String,
                         journalId: ObjectId,
                         issueId: Option[ObjectId], // Non-periodic articles won't have issue ID
                         contributors: Option[List[Contributor]],
                         references: Option[List[Reference]],
                         citedBy: Option[List[Reference]],
                         abstrct: Option[Map[AbstractSection, String]],
                         body: Option[String],
                         accessType: ArticleAccessType,
                         issuedAt: Option[LocalDate],
                         keywords: Option[Seq[String]],
                         webUrl: String,
                         providerId: String,
                         ingestionState: IngestionState,
                         externalProps: ExternalArticleProps)

object Article extends SQLSupportViaImplicit[Article] {

  override val tableName = "articles"
  override val nameConverters = Map(
    "^id$" -> "id",
    "^ids$" -> "ext_ids",
    "^title$" -> "title",
    "^journalId$" -> "journal_id",
    "^issueId$" -> "issue_id",
    "^contributors$" -> "contributors",
    "^references$" -> "refs",
    "^citedBy$" -> "cited_by",
    "^abstrct$" -> "abstract",
    "^body$" -> "body",
    "^accessType$" -> "access_type",
    "^issuedAt$" -> "pub_date",
    "^keywords$" -> "keywords",
    "^webUrl$" -> "web_url",
    "^providerId$" -> "provider_id",
    "^ingestionState$" -> "ingestion_state",
    "^externalProps$" -> "external_props")
}