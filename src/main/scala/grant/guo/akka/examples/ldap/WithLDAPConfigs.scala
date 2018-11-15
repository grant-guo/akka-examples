package grant.guo.akka.examples.ldap

import grant.guo.akka.examples.ldap.WithLDAPConfigs.LdapConfig
import grant.guo.akka.examples.common.Constants._

trait WithLDAPConfigs {
  def ldapConfig: LdapConfig
}

object WithLDAPConfigs {
  case class LdapSSLConfig(serverCertificateUrl:String, port:Int = 636)
  case class LdapConfig(host:String, port:Int, baseDn:String, userDnPatterns: Seq[String], groupKey: String = DEFAULT_GROUP_ATTRIBUTE_KEY, ssl: Option[LdapSSLConfig] = None)
}
