package co.kernelnetworks.medstream.server.models.db

import co.kernelnetworks.medstream.server.db.SQLSupportViaImplicit
import org.bson.types.ObjectId

case class ContactNew(extId: String, name: String, emails: Option[Seq[String]], phones: Option[Seq[String]])

case class Contact(id: ObjectId,
                   ownerId: ObjectId,
                   extId: String,
                   name: String,
                   emails: Option[Seq[String]],
                   phones: Option[Seq[String]],
                   userId: Option[ObjectId])

object Contact extends SQLSupportViaImplicit[Contact] {
  override val tableName = "contacts"
  override val nameConverters = Map(
    "^id$" -> "id",
    "^ownerId$" -> "owner_id",
    "^extId$" -> "ext_id",
    "^name$" -> "name",
    "^emails$" -> "emails",
    "^phones$" -> "phones",
    "^userId$" -> "user_id")
}