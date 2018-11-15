package grant.guo.akka.examples.configuration

import com.typesafe.config.Config

trait WithConfigs {
  def config: Config
}
