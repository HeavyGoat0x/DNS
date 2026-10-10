package fr.uvsq.cprog;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * L'application DNS : enchaîne la lecture, l'exécution et l'affichage des commandes.
 *
 * <p>Les erreurs du DNS et les saisies invalides sont affichées sous la forme
 * {@code ERREUR : message}, puis l'application attend la commande suivante.
 */
public class DnsApp {
  private static final String PROPRIETES = "/dns.properties";
  private static final String CLE_FICHIER = "dns.fichier";

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

  /**
   * Lit le chemin de la base dans le fichier de propriétés {@code dns.properties}.
   *
   * @return le chemin donné par la propriété {@code dns.fichier}
   * @throws IOException si le fichier de propriétés est introuvable, illisible, ou ne contient
   *     pas la propriété {@code dns.fichier}
   */
  static Path cheminBase() throws IOException {
    Properties proprietes = new Properties();
    try (InputStream in = DnsApp.class.getResourceAsStream(PROPRIETES)) {
      if (in == null) {
        throw new IOException("Fichier de propriétés introuvable : " + PROPRIETES);
      }
      proprietes.load(in);
    }

    String nom = proprietes.getProperty(CLE_FICHIER);
    if (nom == null) {
      throw new IOException("Propriété " + CLE_FICHIER + " absente de " + PROPRIETES);
    }
    return Path.of(nom);
  }

  /**
   * Lance l'application sur la base indiquée dans {@code dns.properties}.
   *
   * @param args ignorés
   */
  public static void main(String[] args) {
    try {
      new DnsApp(new Dns(cheminBase()), System.in, System.out).run();
    } catch (IOException | DnsException e) {
      System.err.println("ERREUR : " + e.getMessage());
      System.exit(1);
    }
  }
}
