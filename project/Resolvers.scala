import sbt._

object Resolvers {
  val local = Resolver.mavenLocal

  val local_nexus3_releases = "Local Nexus Releases" at "http://localhost:8081/repository/maven-releases"
  val local_nexus3_snapshots = "Local Nexus Snapshots" at "http://localhost:8081/repository/maven-snapshots"

  val local_nexus3_https_releases = "Local Nexus Releases" at "https://localhost:8443/repository/maven-releases"
  val local_nexus3_https_snapshots = "Local Nexus Snapshots" at "https://localhost:8443/repository/maven-snapshots"

  val host = "http://localhost:8081"
  val nexus3_release = "Nexus3 Releases" at s"${host}/repository/maven-releases/"
  val nexus3_snapshots = "Nexus3 Snapshots" at s"${host}/repository/maven-snapshots/"
  val nexus3_central = "Nexus3 Central" at s"${host}/repository/maven-central/"

}
