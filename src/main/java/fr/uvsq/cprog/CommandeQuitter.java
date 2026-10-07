package fr.uvsq.cprog;

/**
 * Termine l'application : commande {@code quit}.
 *
 * <p>La commande n'arrête pas le programme elle-même : elle déclenche l'action d'arrêt fournie
 * par l'application, qui met fin à sa boucle. La commande reste ainsi testable.
 */
public class CommandeQuitter implements Commande {

  private final Runnable arret;

  /**
   * Crée la commande.
   *
   * @param arret l'action qui met fin à l'application, par exemple la fin de la boucle principale
   */
  public CommandeQuitter(Runnable arret) {
    this.arret = arret;
  }

  @Override
  public String execute() {
    arret.run();
    return "";
  }
}
