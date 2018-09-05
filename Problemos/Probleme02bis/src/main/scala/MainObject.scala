import akka.actor.{Actor, ActorRef, ActorSystem, Kill, PoisonPill, Props, Terminated}

import scala.io.StdIn

object MainObject extends App {

  val system = ActorSystem("MeetupAkka")

  val acteurSuperviseur = system.actorOf(Props[ActeurSuperviseur], "superviseur")

  // Nous allons différencier les deux messages
  val messageSimple = MessageSimple("Ceci est un simple message")
  val messageUrgent = MessageUrgent("Ceci est un message Urgent")


  acteurSuperviseur ! messageSimple
  acteurSuperviseur ! messageUrgent

  // TODO 2: Instantier un message d'arrêt et envoyez le entre plusieurs messages

  acteurSuperviseur ! messageSimple
  acteurSuperviseur ! messageUrgent

  // GracefulStop
  println("taper entrer pour sortir")
  try StdIn.readLine()
  finally system.terminate()


}

class ActeurEnfant extends Actor {
  override def receive(): Receive = {
    case MessageUrgent(message) => {
      import java.io._
      val pw = new PrintWriter(new File("urgent.txt" ))
      pw.write(message)
      pw.close()
      println(s"ActeurEnfant : Le message urgent $message a été sauvegardé !")
    }
    case MessageSimple(text) => println(s"ActeurEnfant : un simple message m'a été envoyé : $text")
  }
  // TODO 3 : Créer un postStop pour réagir à l'arrêt de l'acteur en écrivant un message à la console

}

class ActeurSuperviseur extends Actor {
  val acteurSimple: ActorRef = context.actorOf(Props[ActeurEnfant], "simple-acteur")
  val acteurUrgent: ActorRef = context.actorOf(Props[ActeurEnfant], "urgent-acteur")
  // TODO ajouter deux context.watch pour surveiller les deux acteurs


  override def receive: Receive = {
    case message: MessageSimple =>
      println(s"Superviseur : reçoit un message simple ${message.text}")
      acteurSimple ! message
    case message: MessageUrgent =>
      println(s"Superviseur : reçoit un message urgent ${message.text}")
      acteurUrgent ! message
      // TODO 4 : Quand le superviseur reçoit un message d'arrêt simple, il doit envoier un poisonPill à l'acteur simple
      // TODO et le "unwatch"

      // On reçoit ce message dans le cas où un acteur est arrêté
    case Terminated(unActeurSimple) => println(s" L'acteur simple est mort : ${unActeurSimple.path}")
  }

  // TODO 5 (bonus): créer un comportement qui sera celui de l'acteur superviseur en cas d'arrêt de l'acteur simple
  // indice : Utiliser context.become pour changer de comportement.

}

case class MessageUrgent(text: String)

case class MessageSimple(text: String)

// TODO 1: ajouter une case class d'un message d'arrêt simple


// TODO (bonus +++): Pour aller plus loin, mettre en place un système de poids dans la mailbox qui permet aux message d'être triés
// TODO en fonction de leur priorités
