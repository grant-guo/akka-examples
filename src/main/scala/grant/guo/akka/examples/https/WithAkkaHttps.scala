package grant.guo.akka.examples.https

import java.io.InputStream
import java.net.URL
import java.security.{KeyStore, SecureRandom}

import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import akka.stream.ActorMaterializer
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import scala.util.{Failure, Success, Try}

trait WithAkkaHttps { self: WithAkkaActorSystem with WithHttpsConfigs with WithRoutes =>

  def startHttpsServer():Unit = {
    implicit val actorSystem = system
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    case class HttpsConfig(keyStoreType: String, keyStoreUrl:String, keyStorePassword:String, bindIP:String, bindPort:Int)

    def getHttpsConfig(): HttpsConfig = {
      HttpsConfig(
        keyStoreType = Try(httpsConfig.getString("keystore_type")) match {
          case Success(value) => value
          case Failure(_) => "JKS"
        },
        keyStoreUrl = httpsConfig.getString("keystore_url"),
        keyStorePassword = httpsConfig.getString("keystore_password"),
        bindIP = Try(httpsConfig.getString("bind_ip")) match {
          case Success(value) => value
          case Failure(_) => "0.0.0.0"
        },
        bindPort = httpsConfig.getInt("bind_port")
      )
    }

    val conf = getHttpsConfig()

    val password: Array[Char] = conf.keyStorePassword.toCharArray

    val ks: KeyStore = KeyStore.getInstance(conf.keyStoreType)
    val keystore: InputStream =  new URL(conf.keyStoreUrl).openStream()//new FileInputStream("/Users/xuexuguo/work/ca/grantguo.jks")

    require(keystore != null, "Keystore required!")
    ks.load(keystore, password)

    val kmf: KeyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    kmf.init(ks, password)

    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    tmf.init(ks)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(kmf.getKeyManagers, tmf.getTrustManagers, new SecureRandom())
    val https: HttpsConnectionContext = ConnectionContext.https(sslContext)

    Http().bindAndHandle(routes, interface = conf.bindIP, port = conf.bindPort, connectionContext = https)

  }


}
