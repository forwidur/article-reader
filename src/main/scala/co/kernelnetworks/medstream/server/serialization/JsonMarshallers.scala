package co.kernelnetworks.medstream.server.serialization

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime}

import co.kernelnetworks.medstream.model._
import co.kernelnetworks.medstream.server.models.db._
import co.kernelnetworks.medstream.server.models.shared._
import org.bson.types.ObjectId
import spray.json._

trait JsonMarshallers extends DefaultJsonProtocol {

  implicit val LocalDateFormat = new JsonFormat[LocalDate] {

    private val iso_local_date = DateTimeFormatter.ISO_LOCAL_DATE

    def write(x: LocalDate) = JsString(iso_local_date.format(x))

    def read(value: JsValue) = value match {
      case JsString(x) => LocalDate.parse(x, iso_local_date)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of LocalDate type".format(x.getClass.getName))
    }
  }

  implicit val LocalDateTimeFormat = new JsonFormat[LocalDateTime] {

    private val iso_date_time = DateTimeFormatter.ISO_DATE_TIME

    def write(x: LocalDateTime) = JsString(iso_date_time.format(x))

    def read(value: JsValue) = value match {
      case JsString(x) => LocalDateTime.parse(x, iso_date_time)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of LocalDateTime type".format(x.getClass.getName))
    }
  }

  implicit val IdTypeFormat = new JsonFormat[IdType] {
    def write(x: IdType) = JsString(x.name())

    def read(value: JsValue) = value match {
      case JsString(x) => IdType.valueOf(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of IdType type".format(x.getClass.getName))
    }
  }

  implicit val ArticleAccessTypeFormat = new JsonFormat[ArticleAccessType] {
    def write(x: ArticleAccessType) = JsString(x.name())

    def read(value: JsValue) = value match {
      case JsString(x) => ArticleAccessType.valueOf(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of ArticleAccessType type".format(x.getClass.getName))
    }
  }

  implicit val ContributorTypeFormat = new JsonFormat[ContributorType] {
    def write(x: ContributorType) = JsString(x.name())

    def read(value: JsValue) = value match {
      case JsString(x) => ContributorType.valueOf(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of ContributorType type".format(x.getClass.getName))
    }
  }

  implicit val AbstractSectionFormat = new JsonFormat[AbstractSection] {
    def write(x: AbstractSection) = JsString(x.name())

    def read(value: JsValue) = value match {
      case JsString(x) => AbstractSection.valueOf(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of AbstractSection type".format(x.getClass.getName))
    }
  }

  implicit val ObjectIdFormat = new JsonFormat[ObjectId] {
    def write(x: ObjectId) = JsString(x.toHexString)

    def read(value: JsValue) = value match {
      case JsString(x) => new ObjectId(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of ObjectId type".format(x.getClass.getName))
    }
  }

  implicit val ObjectTypeFormat = new JsonFormat[SignalType] {
    def write(x: SignalType) = JsString(x.name())

    def read(value: JsValue) = value match {
      case JsString(x) => SignalType.valueOf(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of ObjectType type".format(x.getClass.getName))
    }
  }

  implicit val RatingTypeFormat = new JsonFormat[RatingType] {
    def write(x: RatingType) = JsString(x.name())

    def read(value: JsValue) = value match {
      case JsString(x) => RatingType.valueOf(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of RatingType type".format(x.getClass.getName))
    }
  }

  implicit val ClientPlatformTypeFormat = new JsonFormat[ClientPlatformType] {
    def write(x: ClientPlatformType) = JsString(x.getText)

    def read(value: JsValue) = value match {
      case JsString(x) => ClientPlatformType.fromText(x)
      case x => throw new RuntimeException(s"Unexpected type %s on parsing of ClientPlatformType type".format(x.getClass.getName))
    }
  }

  implicit val IdFormat = jsonFormat2(Id.apply)
  implicit val ReferenceFormat = jsonFormat4(Reference)
  implicit val OrganizationFormat = jsonFormat1(Organization)
  implicit val ContributorFormat = jsonFormat5(Contributor)

  implicit val ObjRefFormat = jsonFormat2(ObjRefWithTitle.apply)

  implicit val NewExportedContactFormat = jsonFormat4(ContactNew.apply)
  implicit val ExportedContactFormat = jsonFormat7(Contact.apply)

  implicit val ElectronicAddressFormat = jsonFormat3(ElectronicAddress.apply)
  implicit val DevicesInfoFormat = jsonFormat2(DevicesInfo.apply)

  implicit val ExternalArticlePropsFormat = jsonFormat6(ExternalArticleProps.apply)
  implicit val IdsFormat = jsonFormat8(Ids.apply)
}
