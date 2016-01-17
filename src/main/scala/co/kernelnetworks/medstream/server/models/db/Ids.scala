package co.kernelnetworks.medstream.server.models.db

import co.kernelnetworks.medstream.model.{Id, IdType}
import co.kernelnetworks.medstream.server._

case class Ids(doi: Option[String],
               pmid: Option[Long],
               pmcid: Option[String],
               pii: Option[String],
               eid: Option[String], // Not found in DB
               issn: Option[Seq[String]], // Not found in DB
               nlmta: Option[String], // Not found in DB
               nlmid: Option[String] // Not found in DB
              ) {

  def toSeq: Seq[Id] = {
    Seq(
      doi.map(Id(_, IdType.DOI)),
      pmid.map(v => Id(v.toString, IdType.PMID)),
      pmcid.map(v => Id(v.toString, IdType.PMCID)),
      pii.map(Id(_, IdType.PII)),
      eid.map(Id(_, IdType.EID)),
      nlmid.map(Id(_, IdType.NLMUniqueID)),
      nlmta.map(Id(_, IdType.NLMTA))).flatten ++ issn.map(v => v.map(Id(_, IdType.ISSN))).getOrElse(Nil)
  }

  def normalized: Ids = this.copy(doi = doi.map(_.toLowerCase.trim), pmcid = pmcid.map(_.toUpperCase.trim), pii = pii.map(_.toLowerCase.trim))
}

object Ids {

  final val DOI = "doi"
  final val PMID = "pmid"
  final val PMCID = "pmcid"
  final val PII = "pii"
  final val EID = "eid"
  final val ISSN = "issn"
  final val NLMTA = "nlmta"
  final val NLMID = "nlmid"

  def typeToFieldName(`type`: IdType): String = `type` match {
    case IdType.DOI => DOI
    case IdType.PMID => PMID
    case IdType.PMCID => PMCID
    case IdType.PII => PII
    case IdType.EID => EID
    case IdType.ISSN => ISSN
    case IdType.NLMTA => NLMTA
    case IdType.NLMUniqueID => NLMID
    case _ => "custom"
  }

  def apply(ids: Seq[Id]): Ids = {
    val map = ids.map(v => v.`type` -> v.id).toList.toMultiMap
    Ids(
      map.get(IdType.DOI).map(_.head),
      map.get(IdType.PMID).map(v => v.head.toLong),
      map.get(IdType.PMCID).map(_.head),
      map.get(IdType.PII).map(_.head),
      map.get(IdType.EID).map(_.head),
      map.get(IdType.ISSN).map(_.toSeq),
      map.get(IdType.NLMTA).map(_.head),
      map.get(IdType.NLMUniqueID).map(_.head))
  }
}