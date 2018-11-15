package grant.guo.akka.examples.ldap

import java.io.InputStream
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory

import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.util.ssl.SSLUtil
import javax.net.ssl.TrustManagerFactory

trait WithLDAPConnection { self: WithLDAPConfigs =>

  def getConnection(bindUserDn:String, bindUserPassword:String): LDAPConnection = {

    ldapConfig.ssl match {
      case Some(sslConf) => {
        println("connecting to LDAP server through SSL")
        // enable SSL connection

        val trustStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val trustStoreInputStream: InputStream =  new URL(sslConf.serverCertificateUrl).openStream()
        trustStore.load(null)

        val cf = CertificateFactory.getInstance("X.509")

        def loadCertificatesR(aliasNo:Int):Unit = {
          if(trustStoreInputStream.available() > 0) {
            val cert = cf.generateCertificate(trustStoreInputStream)
            trustStore.setCertificateEntry(s"cert${aliasNo}", cert)
            loadCertificatesR((aliasNo+1)) // there should be only one certificate here
          }
        }

        loadCertificatesR(1)

        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("X509")
        tmf.init(trustStore)

        val socketFactory = (new SSLUtil(tmf.getTrustManagers)).createSSLSocketFactory()

        new LDAPConnection(socketFactory, ldapConfig.host, sslConf.port, bindUserDn, bindUserPassword)
      }
      case None => {
        // set up the normal connection

        new LDAPConnection(ldapConfig.host, ldapConfig.port, bindUserDn, bindUserPassword)

      }
    }
  }
}
