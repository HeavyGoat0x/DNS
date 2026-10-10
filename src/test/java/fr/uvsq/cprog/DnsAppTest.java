package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests unitaires de la classe {@link DnsApp}.
 */
class DnsAppTest {

  @TempDir
  Path dossier;

  private Dns dns;

  @BeforeEach
  void setUp() throws IOException, DnsException {
    dns = new Dns(BaseExemple.ecrit(dossier));
  }

  /**
   * Lance l'application sur la saisie donnée et renvoie tout ce qu'elle a affiché.
   *
   * <p>Le délai maximal fait échouer le test au lieu de le bloquer si la boucle ne s'arrête pas.
   */
  private String execute(String saisie) {
    ByteArrayOutputStream sortie = new ByteArrayOutputStream();
    DnsApp app = new DnsApp(dns,
        new ByteArrayInputStream(saisie.getBytes(StandardCharsets.UTF_8)),
        new PrintStream(sortie, true, StandardCharsets.UTF_8));

    assertTimeoutPreemptively(Duration.ofSeconds(5), app::run);
    return sortie.toString(StandardCharsets.UTF_8).replace(System.lineSeparator(), "\n");
  }

  @Test
  void run_scenarioDuSujet_affichageAttendu() {
    String ecran = execute("www.uvsq.fr\n"
        + "193.51.31.90\n"
        + "ls uvsq.fr\n"
        + "add 193.51.25.24 pikachu.uvsq.fr\n"
        + "ls -a uvsq.fr\n"
        + "add 193.51.25.90 www.uvsq.fr\n"
        + "quit\n");

    assertEquals("> 193.51.31.90\n"
        + "> www.uvsq.fr\n"
        + "> 193.51.25.12 ecampus.uvsq.fr\n"
        + "193.51.31.154 poste.uvsq.fr\n"
        + "193.51.31.90 www.uvsq.fr\n"
        + "> "
        + "> 193.51.25.12 ecampus.uvsq.fr\n"
        + "193.51.25.24 pikachu.uvsq.fr\n"
        + "193.51.31.90 www.uvsq.fr\n"
        + "193.51.31.154 poste.uvsq.fr\n"
        + "> ERREUR : Le nom de machine existe déjà !\n"
        + "> ", ecran);
  }

  @Test
  void run_erreur_continueLaBoucle() {
    String ecran = execute("inconnu.uvsq.fr\nwww.uvsq.fr\nquit\n");

    assertEquals("> ERREUR : Machine inconnue : inconnu.uvsq.fr\n"
        + "> 193.51.31.90\n"
        + "> ", ecran);
  }

  @Test
  void run_saisieInvalide_afficheErreurEtContinue() {
    String ecran = execute("ls\nquit\n");

    assertEquals("> ERREUR : Usage : ls [-a] domaine\n> ", ecran);
  }

  @Test
  void run_finDeSaisieSansQuit_seTermine() {
    assertEquals("> 193.51.31.90\n> ", execute("www.uvsq.fr\n"));
  }

  @Test
  void cheminBase_fichierDeProprietes_renvoieDnsTxt() throws IOException {
    assertEquals(Path.of("dns.txt"), DnsApp.cheminBase());
  }
}
