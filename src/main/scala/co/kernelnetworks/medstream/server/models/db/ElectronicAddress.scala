package co.kernelnetworks.medstream.server.models.db

// Can be either phone or email
case class ElectronicAddress(id: String, isPreferred: Option[Boolean] = None, isVerified: Option[Boolean] = None)
