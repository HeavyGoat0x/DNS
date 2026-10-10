package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests unitaires de la classe {@link DnsTUI}.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
class DnsTUITest {

  @TempDir
  Path dossier;

  private Dns dns;
  private ByteArrayOutputStream sortie;
  private AtomicBoolean arrete;

  @BeforeEach
  void setUp() throws IOException, DnsException {
    dns = new Dns(BaseExemple.ecrit(dossier));
    sortie = new ByteArrayOutputStream();
    arrete = new AtomicBoolean(false);
  }

  /** Crée une interface dont le clavier contient la saisie donnée. */
  private DnsTUI tui(String saisie) {
    return new DnsTUI(dns,
        new ByteArrayInputStream(saisie.getBytes(StandardCharsets.UTF_8)),
        new PrintStream(sortie, true, StandardCharsets.UTF_8),
        () -> arrete.set(true));
  }

  private String ecran() {
    return sortie.toString(StandardCharsets.UTF_8);
  }

  @Test
  void nextCommande_nomQualifie_renvoieSonAdresse() throws DnsException {
    assertEquals("193.51.31.90", tui("www.uvsq.fr\n").nextCommande().execute());
  }

  @Test
  void nextCommande_adresse_renvoieSonNom() throws DnsException {
    assertEquals("www.uvsq.fr", tui("193.51.31.90\n").nextCommande().execute());
  }

  @Test
  void nextCommande_espacesAutour_ignores() throws DnsException {
    assertEquals("193.51.31.90", tui("   www.uvsq.fr   \n").nextCommande().execute());
  }

  @Test
  void nextCommande_lsSansOption_trieParNom() throws DnsException {
    assertEquals("193.51.25.12 ecampus.uvsq.fr\n"
        + "193.51.31.154 poste.uvsq.fr\n"
        + "193.51.31.90 www.uvsq.fr", tui("ls uvsq.fr\n").nextCommande().execute());
  }

  @Test
  void nextCommande_lsAvecOptionA_trieParAdresse() throws DnsException {
    assertEquals("193.51.25.12 ecampus.uvsq.fr\n"
        + "193.51.31.90 www.uvsq.fr\n"
        + "193.51.31.154 poste.uvsq.fr", tui("ls -a uvsq.fr\n").nextCommande().execute());
  }

  @Test
  void nextCommande_add_ajouteLaMachine() throws DnsException {
    String resultat = tui("add 193.51.25.24 pikachu.uvsq.fr\n").nextCommande().execute();

    assertEquals("", resultat);
    assertEquals(new AdresseIP("193.51.25.24"),
        dns.getItem(new NomMachine("pikachu.uvsq.fr")).adresse());
  }

  @ParameterizedTest
  @ValueSource(strings = {"add 1.2.3.4\n", "add 1.2.3.4 a.b c\n", "ls\n", "ls -x uvsq.fr\n",
      "www.uvsq.fr poste.uvsq.fr\n", "1.2.3\n", "pas_un_nom\n"})
  void nextCommande_saisieInvalide_leveIllegalArgument(String saisie) {
    DnsTUI tui = tui(saisie);

    assertThrows(IllegalArgumentException.class, tui::nextCommande);
  }

  @ParameterizedTest
  @ValueSource(strings = {"quit\n", "exit\n", ""})
  void nextCommande_quitOuFinDeSaisie_declencheArret(String saisie) throws DnsException {
    String resultat = tui(saisie).nextCommande().execute();

    assertTrue(arrete.get());
    assertEquals("", resultat);
  }

  @Test
  void nextCommande_ligneVide_neFaitRien() throws DnsException {
    assertEquals("", tui("   \n").nextCommande().execute());
    assertFalse(arrete.get());
  }

  @Test
  void nextCommande_lignesSuccessives_luesUneParUne() throws DnsException {
    DnsTUI tui = tui("www.uvsq.fr\n193.51.31.90\n");

    assertEquals("193.51.31.90", tui.nextCommande().execute());
    assertEquals("www.uvsq.fr", tui.nextCommande().execute());
  }

  @Test
  void nextCommande_lecture_afficheLInvite() {
    tui("www.uvsq.fr\n").nextCommande();

    assertEquals("> ", ecran());
  }

  @Test
  void affiche_resultat_ecritUneLigne() {
    tui("").affiche("193.51.31.90");

    assertEquals("193.51.31.90" + System.lineSeparator(), ecran());
  }

  @Test
  void affiche_chaineVide_rienAffiche() {
    tui("").affiche("");

    assertEquals("", ecran());
  }
}
