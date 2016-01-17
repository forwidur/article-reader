package co.kernelnetworks.medstream.model

case class Reference(text: String, links: Option[List[String]], authors: Option[List[String]] = None, ids : Option[List[Id]] = None)
