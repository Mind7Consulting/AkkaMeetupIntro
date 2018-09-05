import RequestBodyContent.{MessageEditReq, MessageGetReq}
import akka.actor.Actor

class ActorMessaging extends Actor {
  val filename = "urgent.txt"

  override def receive: Receive = {
    case MessageEditReq(message) =>
      val response = FileReadWrite.WriteToFile(message, filename)
      sender() ! response
      context.stop(self)
    case MessageGetReq() =>
      val response = FileReadWrite.ReadFromFile(filename)
      sender() ! response
  }

  override def preStart(): Unit = {
    println(s"starting the actor ${self.path}")
  }
  override def postStop(): Unit = {
    println(s"Stopping the actor ${self.path}")
  }
}

/**
  * Factory qui permet d'utiliser des méthodes d'écriture et de lecture sur/depuis un fichier local
  */
object FileReadWrite {
  def WriteToFile(message: String, filename: String): String = {
    // TODO : Il manque quelque chose ici non ?
    import java.io._
    val pw = new PrintWriter(new File(filename))
    pw.write(message)
    pw.close()
    //  Rappel: dans scala, pas besoin du mot clef return
    s"Le message $message a été correctement sauvegardé !"
  }

  def ReadFromFile(filename: String): String = {
    import scala.io.Source

    Source.fromFile(filename).getLines.mkString
  }
}
