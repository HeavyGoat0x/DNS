package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Crée, pour les tests, une base de démonstration dans un dossier temporaire.
 */
final class BaseExemple {

  private BaseExemple() {
  }

  /**
   * Écrit le fichier de démonstration {@code dns.txt} dans le dossier donné.
   *
   * @param dossier le dossier où créer le fichier, en général un {@code @TempDir}
   * @return le chemin du fichier créé
   * @throws IOException si le fichier ne peut pas être écrit
   */
  static Path ecrit(Path dossier) throws IOException {
    Path fichier = dossier.resolve("dns.txt");
    Files.write(fichier, List.of(
        "www.uvsq.fr 193.51.31.90",
        "poste.uvsq.fr 193.51.31.154",
        "ecampus.uvsq.fr 193.51.25.12",
        "mail.exemple.org 10.0.0.1"));
    return fichier;
  }
}
