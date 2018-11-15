package grant.guo.akka.examples.ldap

import com.unboundid.ldap.sdk.SearchScope
import grant.guo.akka.examples.ldap.WithLDAPAuthentication.Group

import scala.annotation.tailrec
import scala.collection.convert.WrapAsScala
import scala.util.{Failure, Success, Try}
import grant.guo.akka.examples.common.Constants._

trait WithLDAPAuthentication {self: WithLDAPConfigs with WithLDAPConnection =>
  def authenticate(username:String, password:String):Option[Group] = {

    @tailrec
    def authenticateR(patterns: List[String]):Option[Group] = {
      patterns match {
        case Nil => None
        case pattern::tail => {

          val userDn = s"${pattern},${ldapConfig.baseDn}".replace(LDAP_CONFIG_USERNAME_PLACEHOLDER, username)

          Try {
            println(s"Trying to log in by '${userDn}'")

            getConnection(userDn, password)

            //            new LDAPConnection(ldapConfig.host, ldapConfig.port, userDn, password)
          } match {
            case Success(conn) => {
              println(s"Successfully logged in by '${userDn}'")
              // search the groupDn from userDn
              val groupDn =
                WrapAsScala.asScalaBuffer(
                  conn.search(
                    ldapConfig.baseDn,
                    SearchScope.SUB,
                    WithLDAPAuthentication.transformDnToFilter(userDn),
                    ldapConfig.groupKey
                  ).getSearchEntries
                ).map(entry => {
                  entry.getAttributeValue(ldapConfig.groupKey)
                }).toList(0)

              println(s"group dn is ${groupDn}")

              // search group attributes
              val groupAttributes =
                WrapAsScala.asScalaBuffer(
                  conn.search(
                    ldapConfig.baseDn,
                    SearchScope.SUB,
                    WithLDAPAuthentication.transformDnToFilter(groupDn)
                  ).getSearchEntries
                ).flatMap(entry => {
                  WrapAsScala.collectionAsScalaIterable(entry.getAttributes).map(attribute => {
                    (attribute.getName, attribute.getValue)
                  })
                }).toMap

              conn.close()
              Some(
                Group(
                  groupDn.substring(groupDn.indexOf('=') +1, groupDn.indexOf(',')),
                  groupAttributes)
              )
            }
            case Failure(_) => {
              authenticateR(tail)
            }
          }
        }
      }
    }

    authenticateR(ldapConfig.userDnPatterns.toList)

  }

}

object WithLDAPAuthentication{

  def transformDnToFilter(userDn:String): String = {
    s"(${userDn.substring(0, userDn.indexOf(',')).trim})"
  }
  case class Group(name:String, attributes: Map[String, String] = Map.empty[String, String])
}