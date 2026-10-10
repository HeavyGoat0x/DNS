package fr.uvsq.cprog;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * L'application DNS : enchaîne la lecture, l'exécution et l'affichage des commandes.
 *
 * <p>Les erreurs du DNS et les saisies invalides sont affichées sous la forme
 * {@code ERREUR : message}, puis l'application attend la commande suivante.
 */
public class DnsApp {

  private final DnsTUI tui;
  private boolean enCours = true;

  /**
   * Crée l'application.
   *
   * @param dns le DNS interrogé
   * @param entree le flux d'où sont lues les commandes, en général {@code System.in}
   * @param sortie le flux où sont écrits les résultats, en général {@code System.out}
   */
  public DnsApp(Dns dns, InputStream entree, PrintStream sortie) {
    this.tui = new DnsTUI(dns, entree, sortie, () -> enCours = false);
  }

  /**
   * Lit, exécute et affiche les commandes jusqu'à la commande {@code quit} ou la fin de la
   * saisie.
   */
  public void run() {
    while (enCours) {
      try {
        tui.affiche(tui.nextCommande().execute());
      } catch (DnsException | IllegalArgumentException e) {
        tui.affiche("ERREUR : " + e.getMessage());
      }
    }
  }
}
