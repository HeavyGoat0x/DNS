package fr.uvsq.cprog;

/**
 * Une commande du DNS, selon le patron de conception Commande.
 *
 * <p>Chaque action de l'utilisateur est un objet qui sait s'exécuter. La commande ne fait aucun
 * affichage : elle renvoie le texte à afficher, et c'est l'interface utilisateur qui l'affiche.
 */
public interface Commande {

  /**
   * Exécute la commande.
   *
   * @return le texte à afficher, ou une chaîne vide s'il n'y a rien à afficher
   * @throws DnsException si le DNS ne peut pas répondre, par exemple pour une machine inconnue
   */
  String execute() throws DnsException;
}
