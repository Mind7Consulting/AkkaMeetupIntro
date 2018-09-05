object EntryPoint extends App {

  // TODO 04 le code suivant doit compiler/executer sans erreur
  val morty = new Eleve(name = "Morty")
  val rickInstance = Rick()
  rickInstance.donnerCours(morty)

   // TODO 07 le code suivant doit compiler/executer sans erreur
  val moriarty = Moriarty()
  val moriartyEstMechant = ProfesseurFactory.estIlMechant(moriarty)
  println(s"Moriarty est-il méchant ? réponse : ${moriartyEstMechant}")

}

// TODO 01 créer la case Class Eleve avec un string "name" comme parametre
// doc : https://docs.scala-lang.org/tour/case-classes.html
case class Eleve(name: String)

// TODO 03 créer la case class "Rick" qui 'mix-in' le trait Professeur
case class Rick() extends Professeur

trait Professeur {
  // TODO 02 ajouter une méthode "donnerCours" permettant de donner un cours à un élève
  // TODO la méthode prend en paramètre un élève et print "je donne un cours à {le nom de l'élève}"
  // doc: https://docs.scala-lang.org/tour/traits.html
  def donnerCours(eleve: Eleve): Unit = println(s"je donne un cours à ${eleve.name}")
}

// TODO 05 créer la case class Moriarty qui mix in le trait Professeur
// TODO Cette class override la methode donner cours pour printer un autre message
case class Moriarty(mechant : Boolean = true) extends Professeur {
  override def donnerCours(eleve : Eleve): Unit = println("Je suis méchant, je ne donne pas de cours")
}

object ProfesseurFactory {
  def estIlMechant(professeur: Professeur): Boolean = {
    // TODO 06 grace au pattern matching, renvoie false si le Professeur est de type Rick et true sinon
    // doc: https://docs.scala-lang.org/tour/pattern-matching.html
    professeur match {
      case _: Rick => false
      case Moriarty(mechant) => mechant
      case _ => true
    }
  }
}