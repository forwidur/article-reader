package co.kernelnetworks.medstream.model

case class Contributor(firstNameOrTitle: String, lastName: Option[String], email: Option[String], `type`: ContributorType, affilates: Option[List[Organization]])