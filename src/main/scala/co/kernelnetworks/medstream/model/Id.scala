package co.kernelnetworks.medstream.model

case class Id(id: String, `type`: IdType)

object Id {
  def DOI(id: String) = {
    assert(id.trim.nonEmpty, "DOI can't be empty")
    assert(id.contains('/'), "Invalid DOI (missing slash)")
    new Id(id.trim.toLowerCase, IdType.DOI)
  }

  def PMCID(id: String) = {

    val result = new Id(id.trim.toUpperCase, IdType.PMCID)
    assert(result.id.startsWith("PMC") && result.id.length > 3)
    result
  }

  def PMID(id: Long) = new Id(id.toString, IdType.PMID)
}