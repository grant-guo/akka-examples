package grant.guo.akka.examples.ldap

import akka.actor.{Actor, Props}
import com.typesafe.config.Config

import scala.collection.convert.WrapAsScala
import grant.guo.akka.examples.ldap.LDAPAuthenticationActor.UsernamePassword
import grant.guo.akka.examples.ldap.WithLDAPConfigs.LdapConfig
import grant.guo.akka.examples.common.Constants._

import scala.util.{Failure, Success, Try}

class LDAPAuthenticationActor(config: Config) extends Actor with WithLDAPAuthentication with WithLDAPConnection with WithLDAPConfigs {

  override def receive: Receive = {
    case UsernamePassword(username, password) => {
      val sender = context.sender()

      authenticate(username, password) match {
        case Some(group) => sender ! true
        case None => sender ! false
      }

    }
    case _ =>
  }

  override lazy val ldapConfig: LdapConfig = {
    LdapConfig(
      host = config.getString(LDAP_CONFIG_HOST),
      port = config.getInt(LDAP_CONFIG_PORT),
      baseDn = config.getString(LDAP_CONFIG_BASEDN),
      userDnPatterns = WrapAsScala.asScalaBuffer( config.getStringList(LDAP_CONFIG_USERDN_PATTERNS) ).toArray[String],
      groupKey = Try(config.getString(LDAP_CONFIG_GROUP_ATTRIBUTE_KEY)) match {
        case Success(value) => value
        case Failure(exception) => DEFAULT_GROUP_ATTRIBUTE_KEY
      },
      ssl = Try(config.getConfig(LDAP_CONFIG_SSL)) match {
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
}

object LDAPAuthenticationActor{

  case class UsernamePassword(username:String, password:String)

  val name = "LDAP-Authentication-Actor"

  def props(conf: Config): Props = {
    Props(new LDAPAuthenticationActor(conf))
  }
}