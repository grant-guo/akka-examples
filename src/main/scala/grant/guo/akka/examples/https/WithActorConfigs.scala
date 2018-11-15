package grant.guo.akka.examples.https

import com.typesafe.config.Config
import grant.guo.akka.examples.configuration.WithConfigs

trait WithActorConfigs { self: WithConfigs =>
  def actorConfig: Config
}
