package grant.guo.akka.examples

import java.net.URL

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.server.Directives.{complete, headerValueByType}
import com.typesafe.config.ConfigFactory
import grant.guo.akka.examples.configuration.WithConfigs
import grant.guo.akka.examples.https._
import grant.guo.akka.examples.ldap._
import grant.guo.akka.examples.common.Constants._
import grant.guo.akka.examples.ldap.WithLDAPConfigs.LdapConfig

import scala.collection.convert.WrapAsScala
import scala.util.{Failure, Success, Try}

object HttpsServer extends App
                    with WithAkkaHttps
                    with WithRoutes
                    with WithAkkaActorSystem
                    with WithHttpsConfigs
                    with WithActorConfigs
                    with WithLDAPAuthentication
                    with WithLDAPConnection
                    with WithLDAPConfigs
                    with WithConfigs {

  override lazy val config = {

    Option(System.getProperty(SYS_VAR_CONFIG_URL)) match {
      case Some(value) => ConfigFactory.parseURL(new URL(value))
      case None => ConfigFactory.load()
    }

  }

  override lazy val name = "Akka-Https"

  override lazy val routes = {
    headerValueByType[Authorization]() { authorization =>

      authorization match {
        case Authorization(BasicHttpCredentials(username, password)) => {

          authenticate(username, password) match {
            case Some(group) => {
              val name = group.name

              val attributes =
                group.attributes.map(tuple => {
                  s"${tuple._1}: ${tuple._2}"
                }).mkString("\n")

              complete(s"user '${username} belongs to the group '${name}', and it attributes are \n ${attributes} \n")
            }
            case None => complete(s"Failed to authenticate the user '${username}'")
          }

        }
        case _ => complete(s"Not Basic HTTP Credentials")
      }
    }
  }

  override lazy val actorConfig = {
    config.getConfig(CONFIG_PATH_FOR_AKKA_ACTOR)
  }

  override lazy val httpsConfig = {
    config.getConfig(CONFIG_PATH_FOR_AKKA_HTTPS)
  }

  override lazy val ldapConfig = {
    val cc= config.getConfig(CONFIG_PATH_FOR_LDAP)

    LdapConfig(
      host = cc.getString(LDAP_CONFIG_HOST),
      port = cc.getInt(LDAP_CONFIG_PORT),
      baseDn = cc.getString(LDAP_CONFIG_BASEDN),
      userDnPatterns = WrapAsScala.asScalaBuffer( cc.getStringList(LDAP_CONFIG_USERDN_PATTERNS) ),
      groupKey = {
        Try(cc.getString(LDAP_CONFIG_GROUP_ATTRIBUTE_KEY)) match {
          case Success(value) => value
          case Failure(_) => DEFAULT_GROUP_ATTRIBUTE_KEY
        }
      },
      Try(cc.getConfig(LDAP_CONFIG_SSL)) match {
        case Success(value) => {
          Some(
            WithLDAPConfigs.LdapSSLConfig(
              serverCertificateUrl = value.getString(LDAP_CONFIG_SSL_SERVER_CERTIFICATE_URL)
            )
          )
        }
        case Failure(exception) => None
      }
    )
  }

  override val system = ActorSystem(name, actorConfig)


  startHttpsServer()
}
