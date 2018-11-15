package grant.guo.akka.examples.https

import akka.http.scaladsl.server.Route

trait WithRoutes {
  def routes: Route
}
