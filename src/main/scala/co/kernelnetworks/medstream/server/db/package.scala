package co.kernelnetworks.medstream.server

import org.bson.types.ObjectId
import scalikejdbc.WrappedResultSet
import spray.json.{RootJsonReader, JsValue}

package object db {

  implicit class StringImprovements(s: String) {
    def increment = s.map(c => (c + 1).toChar)
  }

  trait SqlTyped[T] {
    def toSql(v: T): Any

    def fromSql(v: Any): T

    def getSqlType: String
  }

  implicit class PimpedSqlType[T](val any: T) extends AnyVal {
    def toSqlType(implicit conv: SqlTyped[T]): Any =
      conv.toSql(any)
  }

  implicit class PimpedWrappedResultSet[T](val rs: WrappedResultSet) extends AnyVal {

    def json(columnLabel: String)(implicit conv: SqlTyped[JsValue]): JsValue =
      conv.fromSql(rs.any(columnLabel))

    def jsonOpt(columnLabel: String)(implicit conv: SqlTyped[JsValue]): Option[JsValue] = {
      val arr = rs.any(columnLabel)
      if (arr != null)
        Option(conv.fromSql(arr))
      else
        None
    }

    def jsonObj[B](columnLabel: String)(implicit conv: SqlTyped[JsValue], reader: RootJsonReader[B]): B =
      reader.read(json(columnLabel))

    def jsonObjOpt[B](columnLabel: String)(implicit conv: SqlTyped[JsValue], reader: RootJsonReader[B]): Option[B] = {
      val arr = rs.any(columnLabel)
      if (arr != null)
        Option(reader.read(json(columnLabel)))
      else
        None
    }

    def oid(columnLabel: String)(implicit conv: SqlTyped[ObjectId]): ObjectId =
      conv.fromSql(rs.any(columnLabel))

    def oidOpt(columnLabel: String)(implicit conv: SqlTyped[ObjectId]): Option[ObjectId] = {
      val arr = rs.any(columnLabel)
      if (arr != null)
        Option(conv.fromSql(arr))
      else
        None
    }

    def array2[B](columnLabel: String)(implicit conv: SqlTyped[B]): Seq[B] =
      rs.array(columnLabel).getArray.asInstanceOf[Array[Any]].map(conv.fromSql)

    def array2Opt[B](columnLabel: String)(implicit conv: SqlTyped[B]): Option[Seq[B]] =
      for (arr <- rs.arrayOpt(columnLabel)) yield
        arr.getArray.asInstanceOf[Array[Any]].map(conv.fromSql)

    def typed[B](columnLabel: String)(implicit conv: SqlTyped[B]): B =
      conv.fromSql(rs.any(columnLabel))

    def typedOpt[B](columnLabel: String)(implicit conv: SqlTyped[B]): Option[B] =
      rs.anyOpt(columnLabel).map(conv.fromSql)
  }

}
