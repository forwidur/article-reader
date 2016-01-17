package co.kernelnetworks.medstream.server.db

import java.sql.{Timestamp, Date}
import java.time.{LocalDateTime, LocalDate}

import co.kernelnetworks.medstream.server.serialization.JsonMarshallers
import org.bson.types.ObjectId
import org.postgresql.util.PGobject
import scala.reflect.runtime.universe._
import spray.json._

trait ScalikeJdbcHelpers { this : JsonMarshallers =>

  implicit val ObjectIdToSql = new SqlTyped[ObjectId] {
    override def toSql(v: ObjectId): Any = v.toByteArray
    override def fromSql(v: Any) = new ObjectId(v.asInstanceOf[Array[Byte]])
    override def getSqlType = "bytea"
  }

  implicit val StringToSql = new SqlTyped[String] {
    override def toSql(v: String): Any = v
    override def fromSql(v: Any) = v.asInstanceOf[String]
    override def getSqlType = "varchar"
  }

  implicit val LocalDateToSql = new SqlTyped[LocalDate] {
    override def toSql(v: LocalDate): Any = Date.valueOf(v)
    override def fromSql(v: Any) = v.asInstanceOf[java.sql.Date].toLocalDate
    override def getSqlType = "date"
  }

  implicit val LocalDateTimeToSql = new SqlTyped[LocalDateTime] {
    override def toSql(v: LocalDateTime): Any = Timestamp.valueOf(v)
    override def fromSql(v: Any) = v.asInstanceOf[java.sql.Timestamp].toLocalDateTime
    override def getSqlType = "timestamp"
  }

  implicit val JsValueToSql = new SqlTyped[JsValue] {
    override def toSql(v: JsValue): Any = {
      val pgobj = new PGobject()
      pgobj.setType(getSqlType)
      pgobj.setValue(v.compactPrint)
      pgobj
    }
    override def fromSql(v: Any) =
      v match {
        case v: PGobject => v.getValue.parseJson
        case v: String => v.parseJson
        case _ => throw new RuntimeException(s"JsValueToSql got value of unsupported type: ${v.getClass.getSimpleName}")
      }
    override def getSqlType = "jsonb"
  }

  implicit def getSimpleTypesConverter[T <: AnyVal : TypeTag] : SqlTyped[T] = new SqlTyped[T] {
    override def toSql(value: T): Any = value
    override def fromSql(v: Any) = v.asInstanceOf[T]

    override def getSqlType = typeOf[T] match {
      case tpe if tpe =:= typeOf[Byte] || tpe =:= typeOf[Short] => "int2"
      case tpe if tpe =:= typeOf[Int] => "int4"
      case tpe if tpe =:= typeOf[Long] => "int8"
      case tpe if tpe =:= typeOf[Float] => "float4"
      case tpe if tpe =:= typeOf[Double] => "float8"
      case tpe if tpe =:= typeOf[Char] => "int2" // Not sure about this
      case tpe if tpe =:= typeOf[Boolean] => "bit"
      case tpe => throw new RuntimeException(s"Unknown AnyVal type: $tpe")
    }
  }

  implicit def getOptionConverter[T](implicit conv : SqlTyped[T]) : SqlTyped[Option[T]] = new SqlTyped[Option[T]] {
    override def toSql(v: Option[T]): Any =
      v.map(conv.toSql).orNull

    override def fromSql(v: Any) = if (v == null) None else Option(v.asInstanceOf[T])

    override def getSqlType = conv.getSqlType
  }

  implicit def seqToSql[T : SqlTyped] = toSqlviaSeq[Seq[T], T](seq => Seq(seq :_*))
  implicit def iterableToSql[T :SqlTyped]   = toSqlviaSeq[Iterable[T], T](seq => Iterable(seq :_*))
  implicit def indexedSeqToSql[T :SqlTyped] = toSqlviaSeq[IndexedSeq[T], T](seq => IndexedSeq(seq :_*))
  implicit def immSetToSql[T :SqlTyped]        = toSqlviaSeq[Set[T], T](seq => Set(seq :_*))
  implicit def listToSql[T : SqlTyped] = toSqlviaSeq[List[T], T](seq => List(seq :_*))

  def toSqlviaSeq[I <: Iterable[T], T : SqlTyped](f: Seq[T] => I)(implicit conv : SqlTyped[T]): SqlTyped[I] = new SqlTyped[I] {
    override def toSql(v: I) = SimpleArrayUtils2.mkArray(buildArrayStr)(conv.getSqlType, v.map(conv.toSql).toSeq)
//      new SimpleArray(conv.getSqlType, v.map(conv.toSql).toSeq, (param : Iterable[Any]) => param.mkString(","))
    override def getSqlType = conv.getSqlType + "[]"

    override def fromSql(v: Any) = f(v.asInstanceOf[Array[T]].map(conv.fromSql).toSeq)

    protected def buildArrayStr(vList: Seq[Any]): String = SimpleArrayUtils2.mkString[Any](_.toString)(vList)
  }

  implicit def getSprayConverter[T <: AnyRef : RootJsonWriter : RootJsonReader](implicit reader: RootJsonReader[T], writer: RootJsonWriter[T], jsValueToSql : SqlTyped[JsValue]) : SqlTyped[T] = new SqlTyped[T]{
    override def toSql(v: T) = jsValueToSql.toSql(writer.write(v))
    override def getSqlType = jsValueToSql.getSqlType

    override def fromSql(v: Any) = reader.read(jsValueToSql.fromSql(v))
  }

}
