package fr.uvsq.cprog;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * L'interface utilisateur en mode texte du DNS.
 *
 * <p>Elle lit les lignes saisies, les transforme en {@link Commande} et affiche les résultats.
 * Elle n'exécute aucune commande elle-même : c'est le rôle de l'application.
 *
 * <p>Commandes reconnues :
 * <ul>
 *   <li>{@code nom.qualifie} : affiche l'adresse IP de la machine ;</li>
 *   <li>{@code adresse.ip} : affiche le nom qualifié de la machine ;</li>
 *   <li>{@code ls [-a] domaine} : liste les machines du domaine, par nom ou par adresse ;</li>
 *   <li>{@code add adresse.ip nom.qualifie} : ajoute une machine ;</li>
 *   <li>{@code quit} ou {@code exit}, ou la fin de la saisie : termine l'application.</li>
 * </ul>
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class DnsTUI {
  private static final String INVITE = "> ";

  /** Une saisie faite uniquement de chiffres et de points désigne une adresse IP. */
  private static final Pattern ADRESSE = Pattern.compile("[\\d.]+");

  private final Dns dns;
  private final Scanner lecteur;
  private final PrintStream sortie;
  private final Runnable arret;

  /**
   * Crée l'interface utilisateur.
   *
   * @param dns le DNS interrogé par les commandes
   * @param entree le flux d'où sont lues les commandes, en général {@code System.in}
   * @param sortie le flux où sont écrits l'invite et les résultats, en général {@code System.out}
   * @param arret l'action qui termine l'application, transmise à la commande {@code quit}
   */
  public DnsTUI(Dns dns, InputStream entree, PrintStream sortie, Runnable arret) {
    this.dns = dns;
    this.lecteur = new Scanner(entree, StandardCharsets.UTF_8);
    this.sortie = sortie;
    this.arret = arret;
  }

  /**
   * Affiche l'invite, lit la ligne suivante et renvoie la commande correspondante.
   *
   * <p>Une ligne vide donne une commande qui ne fait rien. La fin de la saisie donne la commande
   * {@code quit}.
   *
   * @return la commande à exécuter
   * @throws IllegalArgumentException si la ligne n'est pas une commande valide
   */
  public Commande nextCommande() {
    sortie.print(INVITE);
    sortie.flush();

    if (!lecteur.hasNextLine()) {
      return new CommandeQuitter(arret);
    }
    String ligne = lecteur.nextLine().trim();
    if (ligne.isEmpty()) {
      return () -> "";
    }

    String[] mots = ligne.split("\\s+");
    return switch (mots[0]) {
      case "quit", "exit" -> new CommandeQuitter(arret);
      case "ls" -> analyseLs(mots);
      case "add" -> analyseAdd(mots);
      default -> analyseRecherche(mots);
    };
  }

  /**
   * Affiche le résultat d'une commande, sauf s'il est vide.
   *
   * @param resultat le texte à afficher
   */
  public void affiche(String resultat) {
    if (!resultat.isEmpty()) {
      sortie.println(resultat);
    }
  }

  private Commande analyseLs(String[] mots) {
    if (mots.length == 2) {
      return new CommandeListeDomaine(dns, mots[1], false);
    }
    if (mots.length == 3 && "-a".equals(mots[1])) {
      return new CommandeListeDomaine(dns, mots[2], true);
    }
    throw new IllegalArgumentException("Usage : ls [-a] domaine");
  }

  private Commande analyseAdd(String[] mots) {
    if (mots.length != 3) {
      throw new IllegalArgumentException("Usage : add adresse.ip nom.qualifie");
    }
    return new CommandeAjout(dns, new AdresseIP(mots[1]), new NomMachine(mots[2]));
  }

  private Commande analyseRecherche(String[] mots) {
    if (mots.length != 1) {
      throw new IllegalArgumentException("Commande inconnue : " + String.join(" ", mots));
    }
    String mot = mots[0];
    if (ADRESSE.matcher(mot).matches()) {
      return new CommandeRechercheNom(dns, new AdresseIP(mot));
    }
    return new CommandeRechercheAdresse(dns, new NomMachine(mot));
  }
}
