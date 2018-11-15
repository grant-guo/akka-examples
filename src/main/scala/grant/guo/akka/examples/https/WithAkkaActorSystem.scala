package grant.guo.akka.examples.https

import akka.actor.ActorSystem

trait WithAkkaActorSystem { self: WithActorConfigs =>
  def name:String
  def system: ActorSystem
}
