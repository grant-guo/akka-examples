package grant.guo.akka.examples

import java.net.URL

import com.typesafe.config.ConfigFactory
import grant.guo.akka.examples.common.Constants._
import grant.guo.akka.examples.ldap.WithLDAPConfigs
import grant.guo.akka.examples.ldap.WithLDAPConfigs.LdapConfig
import grant.guo.akka.examples.ldap.{WithLDAPAuthentication, WithLDAPConfigs, WithLDAPConnection}

import scala.collection.convert.WrapAsScala
import scala.util.{Failure, Success, Try}

object LDAPAuthenticationMain$ extends App with WithLDAPAuthentication with WithLDAPConfigs with WithLDAPConnection{

  lazy val conf = Option(System.getProperty(SYS_VAR_CONFIG_URL)) match {
    case Some(value) => ConfigFactory.parseURL(new URL(value))
    case None => ConfigFactory.load()
  }

  override lazy val ldapConfig = {

    val cc = conf.getConfig(CONFIG_PATH_FOR_LDAP)

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


  val username = conf.getString(s"${CONFIG_PREFIX}.examples.ldap.username")
  val password = conf.getString(s"${CONFIG_PREFIX}.examples.ldap.password")
  authenticate(username, password) match {
    case Some(group) => {
      println(s"Authentication is successful for the user '${username}'")

      println(s"here are the attributes of the group '${group.name}'")
      group.attributes.foreach(att => {
        println(s"${att._1}: ${att._2}")
      })
    }
    case None => {
      println(s"Authentication is failed for the user '${username}'")
    }
  }
}
