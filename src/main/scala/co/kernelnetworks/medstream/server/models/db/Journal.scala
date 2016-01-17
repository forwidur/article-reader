package co.kernelnetworks.medstream.server.models.db

import co.kernelnetworks.medstream.server.db.SQLSupportViaImplicit
import org.bson.types.ObjectId

case class Journal(id: ObjectId,
                   ids: Ids,
                   title: String,
                   publisherName: String,
                   PMCUrl: Option[String],
                   rawTopics: Option[Seq[String]],
                   rawData: Option[String])

object Journal extends SQLSupportViaImplicit[Journal] {

  override val tableName = "journals"
  override val nameConverters = Map(
    "^id$" -> "id",
    "^ids$" -> "ext_ids",
    "^title$" -> "title",
    "^publisherName$" -> "publisher_name",
    "^PMCUrl$" -> "pmc_url",
    "^rawTopics$" -> "raw_topics",
    "^rawData$" -> "raw_data")
}