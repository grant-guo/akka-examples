import dependencies._

name := "akka-example"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  typesafeConfig,
  akkaHttp,
  akkaStream,
  akkaHttpCaching,
  oidcOAuth2,
  unboundIDLdap,
  akkaTestkit % Test,
  akkaHttpTestkit % Test

)

assemblyJarName in assembly := s"${organization.value}.${name.value}-${version.value}.jar"
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)