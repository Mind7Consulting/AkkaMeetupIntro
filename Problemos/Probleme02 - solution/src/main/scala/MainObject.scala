import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.io.StdIn

object MainObject extends App {

  // TODO 02 Créer un message urgent et un message simple
  val messageSimple = MessageSimple("Ceci est un simple message")
  val messageUrgent = MessageUrgent("Ceci est un message Urgent")

  // TODO 03 Créer un systeme d'acteur
  val system = ActorSystem("MeetupAkka")

  // TODO 03 bis Créer une instance de ActeurSimple (elle doit renvoyer une référence)
  val acteurSimple = system.actorOf(Props[ActeurSimple], "acteur-simple")

  // TODO 04 Envoyer à la référence de acteur simple un message urgent et un message simple
  // TODO executer ensuite pour vérifier le résultat
  acteurSimple ! messageSimple
  acteurSimple ! messageUrgent

  // TODO 07 Créer un acteur ActeurSuperviseur et envoyez lui les deux messages
  val acteurSuperviseur = system.actorOf(Props[ActeurSuperviseur], "superviseur")

  // Nous allons différencier les deux messages
  val messageSimple2 = MessageSimple("Ceci est le 2ème simple message")
  val messageUrgent2 = MessageUrgent("Ceci est le 2ème message Urgent")

  acteurSuperviseur ! messageSimple2
  acteurSuperviseur ! messageUrgent2

  // GracefulStop
  println("taper entrer pour sortir")
  try StdIn.readLine()
  finally system.terminate()


}

// TODO 01 Créer un acteur "ActeurSimple" qui reçoit des messages de type Message, MessageUrgent et MessageSimple
// TODO et print dans la console le type de message reçu
class ActeurSimple extends Actor {
  // TODO 06 Modifier l'acteur ActeurSimple pour que lorsqu'il reçoit un message urgent, il écrive le message
  // TODO dans un fichier nommé urgent.txt et print le message (utiliser l'API java pour écrire dans un fichier)
  override def receive(): Receive = {
    case MessageUrgent(message) => {
      import java.io._
      val pw = new PrintWriter(new File("urgent.txt" ))
      pw.write(message)
      pw.close()
      println(s"Le message urgent $message a été sauvegardé !")
    }
    case MessageSimple(text) => println(s"un simple message m'a été envoyé : $text")
  }
}


// TODO 05 créer un Acteur "ActeurSuperviseur" qui possède deux valeurs (val) d'acteurs immutables:
// TODO "acteurUrgent" et "acteurSimple"  qui sont tous les deux des instances de ActeurSimple
// TODO Lorsque l'acteur superviseur reçoit un message urgent, il envoie le message à l'acteur urgent.
// TODO même chose pour le simple
class ActeurSuperviseur extends Actor {
  val acteurSimple: ActorRef = context.actorOf(Props[ActeurSimple], "simple-acteur")
  val acteurUrgent: ActorRef = context.actorOf(Props[ActeurSimple], "urgent-acteur")

  override def receive: Receive = {
    case message: MessageSimple =>
      println(s"Le superviseur reçoit un message simple ${message.text}")
      acteurSimple ! message
    case message: MessageUrgent =>
      println(s"Le superviseur reçoit un message urgent ${message.text}")
      acteurUrgent ! message
    case message => println(s"Message reçu de $sender : $message")
  }
}

case class MessageUrgent(text: String)

case class MessageSimple(text: String)