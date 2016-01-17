package co.kernelnetworks.medstream.server.models.db

import co.kernelnetworks.medstream.server.db.SQLSupportViaImplicit
import org.bson.types.ObjectId

case class DevicesInfo(deviceIds: Option[Map[String, Seq[String]]], awsSNSEnpoints: Option[Seq[String]])

case class User(id: ObjectId,
                name: String,
                email: Option[String],
                pwdHash: Option[String],
                token: Option[ObjectId],
                emails: Option[Seq[ElectronicAddress]],
                phones: Option[Seq[ElectronicAddress]],
                devicesInfo: DevicesInfo,
                company: Option[String],
                primarySpecialtyId: Option[String],
                secondarySpecialtyId: Option[String],
                subscribedTopicsIds: Option[Seq[String]])

object User extends SQLSupportViaImplicit[User] {

  override val tableName = "users"
  override val nameConverters = Map(
    "^id$" -> "id",
    "^name$" -> "name",
    "^email$" -> "email",
    "^pwdHash$" -> "pwd_hash",
    "^token$" -> "token",
    "^emails$" -> "emails",
    "^phones$" -> "phones",
    "^devicesInfo$" -> "devices_info",
    "^company$" -> "company",
    "^primarySpecialtyId$" -> "specialty_id_1",
    "^secondarySpecialtyId$" -> "specialty_id_2",
    "^subscribedTopicsIds$" -> "topic_ids")
}