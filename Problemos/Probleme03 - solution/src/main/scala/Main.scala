import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.duration._
import scala.io.StdIn

object Main extends App {
  // on crée un system d'acteur, un actor materializer ainsi qu'un context d'execution.
  implicit val system = ActorSystem("last-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  // TODO 01 spécifier un implicit val "timeout" avec akka.util.Timeout
  // TODO n'oubliez pas d'importer scala.concurrent.duration._ pour pouvoir définir une unité de temps dans le timeout
  implicit val timeout = Timeout(5 seconds)

  // TODO 02 créer un ActorMessaging pour la route GET et un pour le POST
  val actorMessaging = system.actorOf(Props[ActorMessaging], "actor-get")
  // TODO 03 créer la route get "/message" qui envoie un MessageGetReq à un acteur
  // TODO l'envoie se fera en "ask" et non en "fire and forget" (importer  akka.pattern.ask pour utiliser le pattern)

  // TODO 04 Faire une route post "/message" qui modifie le message dans le fichier text avec le contenu du body de la requête
  val route =
    path("message") {
      get {
        val responseFuture = (actorMessaging ? RequestBodyContent.MessageGetReq()).mapTo[String]
        complete(responseFuture)
      } ~
        post {
          entity(as[String]) { message =>
            // This is the request per actor pattern
            val actorPerRequestStyle = system.actorOf(Props[ActorMessaging], "actor-post")
            val responseFuture = (actorPerRequestStyle ? RequestBodyContent.MessageEditReq(message)).mapTo[String]
            complete(responseFuture)
          }
        }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}

// types de messages à envoyer aux acteurs
object RequestBodyContent {

  // the requests
  case class MessageGetReq()

  case class MessageEditReq(message: String)

}
