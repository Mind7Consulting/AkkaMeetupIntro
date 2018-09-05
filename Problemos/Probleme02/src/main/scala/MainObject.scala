object MainObject extends App {

  // TODO 02 Créer un message urgent et un message simple

  // TODO 03 Créer un systeme d'acteur

  // TODO 03bis Créer une instance de ActeurSimple (elle doit renvoyer une référence)

  // TODO 04 Envoyer à la référence de acteur simple un message urgent et un message simple
  // TODO executer ensuite pour vérifier le résultat

  // TODO 07 Créer un acteur ActeurSuperviseur et envoyez lui les deux messages
}

// TODO 01 Créer un acteur "ActeurSimple" qui reçoit des messages de type Message, MessageUrgent et MessageSimple
// TODO et print dans la console le type de message reçu

// TODO 06 Modifier l'acteur ActeurSimple pour que lorsqu'il reçoit un message urgent, il écrive le message
// TODO dans un fichier nommé urgent.txt et print le message (utiliser l'API java pour écrire dans un fichier)



// TODO 05 créer un Acteur "ActeurSuperviseur" qui possède deux valeurs (val) d'acteurs immutables:
// TODO "acteurUrgent" et "acteurSimple"  qui sont tous les deux des instances de ActeurSimple
// TODO Lorsque l'acteur superviseur reçoit un message urgent, il envoie le message à l'acteur urgent.
// TODO même chose pour le simple


case class MessageUrgent(text: String)

case class MessageSimple(text: String)