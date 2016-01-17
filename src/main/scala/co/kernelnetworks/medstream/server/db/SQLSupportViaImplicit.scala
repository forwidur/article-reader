package co.kernelnetworks.medstream.server.db

import scalikejdbc.{WrappedResultSet, SQLSyntaxSupport, SyntaxProvider, ResultName}

trait SQLSupportViaImplicit[T] extends SQLSyntaxSupport[T] {

  def apply(o: SyntaxProvider[T])(rs: WrappedResultSet)(implicit reader : (String => String, WrappedResultSet) => T): T =
    apply(o.resultName)(rs)

  def apply(o: ResultName[T])(rs: WrappedResultSet)(implicit reader : (String => String, WrappedResultSet) => T): T =
    reader(o.column, rs)

  def apply(rs: WrappedResultSet)(implicit reader : (String => String, WrappedResultSet) => T): T =
    reader(s => s, rs)
}
