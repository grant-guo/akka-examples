package grant.guo.akka.examples.common

object Constants {
  val DEFAULT_ACTOR_SYSTEM_NAME = "TrustAuthority"
  val DEFAULT_CONFIG_FILE_NAME = "application.conf"
  val CONFIG_PREFIX = "grant.guo.akka.examples"
  val CONFIG_PATH_FOR_ACTOR = "actor"
  val CONFIG_PATH_FOR_SERVER = "server"
  val SYS_VAR_CONFIG_URL = "config_url"
  val CONFIG_PATH_FOR_AKKA_ACTOR = s"${CONFIG_PREFIX}.akka"
  val CONFIG_PATH_FOR_AKKA_HTTPS = s"${CONFIG_PREFIX}.https"
  val CONFIG_PATH_FOR_LDAP = s"${CONFIG_PREFIX}.authentication.ldap"

  val LDAP_CONFIG_HOST = "host"
  val LDAP_CONFIG_PORT = "port"
  val LDAP_CONFIG_BASEDN = "basedn"
  val LDAP_CONFIG_USERDN_PATTERNS = "userdn_patterns"
  val LDAP_CONFIG_USERNAME_PLACEHOLDER = "$"
  val LDAP_CONFIG_GROUP_ATTRIBUTE_KEY = "group_attribute_key"
  val DEFAULT_GROUP_ATTRIBUTE_KEY = "memberOf"
  val LDAP_CONFIG_SSL = "ssl"
  val LDAP_CONFIG_SSL_SERVER_CERTIFICATE_URL = "server_certificate_url"
}
