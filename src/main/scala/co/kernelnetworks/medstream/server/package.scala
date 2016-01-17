package co.kernelnetworks.medstream

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.jasypt.util.password.StrongPasswordEncryptor

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

package object server extends LazyLogging {

  implicit class Pairs[A, B](p: Seq[(A, B)]) {
    def toMultiMap: Map[A, Seq[B]] = p.groupBy(_._1).mapValues(_.map(_._2))
  }

  def getIntDockerVariable(name: String, default: Int): Int =
    Try {
      System.getenv(name).toInt
    }.getOrElse {
      logger.warn(s"System variable $name is missing or invalid. Default port $default will be used.")
      default
    }

  def getStringDockerVariable(name: String, default: String): String =
    Option(System.getenv(name)).getOrElse {
      logger.warn(s"System variable $name is missing or invalid. Default port $default will be used.")
      default
    }
}
