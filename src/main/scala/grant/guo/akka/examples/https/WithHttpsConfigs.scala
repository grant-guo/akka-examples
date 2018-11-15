package grant.guo.akka.examples.https

import com.typesafe.config.Config
import grant.guo.akka.examples.configuration.WithConfigs

trait WithHttpsConfigs { self : WithConfigs =>
  def httpsConfig: Config
}
