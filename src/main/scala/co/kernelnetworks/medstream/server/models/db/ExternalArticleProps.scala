package co.kernelnetworks.medstream.server.models.db

final case class ExternalArticleProps(abstrct: Option[List[(String, String)]],
                                      topics: Option[List[String]],
                                      `type`: Option[List[String]], // Research, Perspective, Special Article, Perspective, Original Article
                                      sourceUrl: String,
                                      externalIssueId: Option[String],
                                      rawData: Option[String])
