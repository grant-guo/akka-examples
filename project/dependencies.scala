
import sbt._

object dependencies {
  val akkaHttpVersion = "10.1.3"
  val akkaActorVersion = "2.5.12"

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaActorVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaActorVersion
  val akkaHttpCaching = "com.typesafe.akka" %% "akka-http-caching" % akkaHttpVersion
  val oidcOAuth2 = "com.nimbusds" % "oauth2-oidc-sdk" % "5.64.4"
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaActorVersion
  val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
  val typesafeConfig = "com.typesafe" % "config" % "1.3.3"
  val jsch = "com.jcraft" % "jsch" % "0.1.54"
  val sshdSftp = "org.apache.sshd" % "sshd-sftp" % "2.0.0"
  val unboundIDLdap = "com.unboundid" % "unboundid-ldapsdk" % "4.0.7"
  val googleTink = "com.google.crypto.tink" % "tink" % "1.2.0"
  val mitreIdConnect = "org.mitre" % "openid-connect-server" % "1.3.2"

}
