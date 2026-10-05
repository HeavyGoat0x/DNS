package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests unitaires de la classe {@link Dns}.
 */
class DnsTest {

  @TempDir
  Path dossier;

  private Path fichier;
  private Dns dns;

  @BeforeEach
  void setUp() throws IOException, DnsException {
    fichier = BaseExemple.ecrit(dossier);
    dns = new Dns(fichier);
  }

  @Test
  void constructeur_fichierAbsent_leveDnsException() {
    assertThrows(DnsException.class, () -> new Dns(dossier.resolve("absent.txt")));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "poste.uvsq.fr pas.une.adresse",
      "poste.uvsq.fr",
      "poste.uvsq.fr 193.51.31.154 en.trop",
      "pas_un_nom 193.51.31.154",
      "www.uvsq.fr 193.51.31.154",
      "poste.uvsq.fr 193.51.31.90"})
  void constructeur_deuxiemeLigneInvalide_leveDnsExceptionAvecNumero(String ligne)
      throws IOException {
    Path invalide = dossier.resolve("invalide.txt");
    Files.write(invalide, List.of("www.uvsq.fr 193.51.31.90", ligne));

    DnsException e = assertThrows(DnsException.class, () -> new Dns(invalide));

    assertTrue(e.getMessage().startsWith("Ligne 2 invalide : " + ligne), e.getMessage());
  }

  @Test
  void constructeur_lignesVides_ignorees() throws IOException, DnsException {
    Path avecVides = dossier.resolve("vides.txt");
    Files.write(avecVides, List.of("", "www.uvsq.fr 193.51.31.90", "   "));

    assertEquals(1, new Dns(avecVides).getItems("uvsq.fr").size());
  }

  @Test
  void getItem_nomConnu_renvoieSonAdresse() throws DnsException {
    DnsItem item = dns.getItem(new NomMachine("www.uvsq.fr"));

    assertEquals(new AdresseIP("193.51.31.90"), item.adresse());
  }

  @Test
  void getItem_adresseConnue_renvoieSonNom() throws DnsException {
    DnsItem item = dns.getItem(new AdresseIP("193.51.31.90"));

    assertEquals(new NomMachine("www.uvsq.fr"), item.nom());
  }

  @Test
  void getItem_toutesLesLignes_chargees() throws DnsException {
    assertEquals(new NomMachine("ecampus.uvsq.fr"),
        dns.getItem(new AdresseIP("193.51.25.12")).nom());
    assertEquals(new AdresseIP("10.0.0.1"),
        dns.getItem(new NomMachine("mail.exemple.org")).adresse());
  }

  @Test
  void getItem_nomInconnu_leveDnsException() {
    DnsException e = assertThrows(DnsException.class,
        () -> dns.getItem(new NomMachine("inconnu.uvsq.fr")));

    assertEquals("Machine inconnue : inconnu.uvsq.fr", e.getMessage());
  }

  @Test
  void getItem_adresseInconnue_leveDnsException() {
    DnsException e = assertThrows(DnsException.class,
        () -> dns.getItem(new AdresseIP("1.2.3.4")));

    assertEquals("Adresse inconnue : 1.2.3.4", e.getMessage());
  }

  @Test
  void getItems_domaine_renvoieSeulementSesMachines() {
    Set<NomMachine> noms = dns.getItems("uvsq.fr").stream().map(DnsItem::nom)
        .collect(Collectors.toSet());

    assertEquals(Set.of(new NomMachine("www.uvsq.fr"), new NomMachine("poste.uvsq.fr"),
        new NomMachine("ecampus.uvsq.fr")), noms);
  }

  @Test
  void getItems_domaineEnMajuscules_trouveLesMachines() {
    assertEquals(3, dns.getItems("UVSQ.Fr").size());
  }

  @Test
  void getItems_domaineInconnu_listeVide() {
    assertTrue(dns.getItems("random.org").isEmpty());
  }

  @Test
  void getItems_sousDomaine_nonInclus() {
    assertTrue(dns.getItems("fr").isEmpty());
  }

  @Test
  void addItem_nouvelleMachine_trouveeParNomEtParAdresse() throws DnsException {
    dns.addItem(new AdresseIP("193.51.25.24"), new NomMachine("pikachu.uvsq.fr"));

    assertEquals(new AdresseIP("193.51.25.24"),
        dns.getItem(new NomMachine("pikachu.uvsq.fr")).adresse());
    assertEquals(new NomMachine("pikachu.uvsq.fr"),
        dns.getItem(new AdresseIP("193.51.25.24")).nom());
  }

  @Test
  void addItem_nouvelleMachine_fichierActualise() throws IOException, DnsException {
    dns.addItem(new AdresseIP("193.51.25.24"), new NomMachine("pikachu.uvsq.fr"));

    List<String> lignes = Files.readAllLines(fichier);
    assertEquals("pikachu.uvsq.fr 193.51.25.24", lignes.get(lignes.size() - 1));

    Dns relu = new Dns(fichier);
    assertEquals(new AdresseIP("193.51.25.24"),
        relu.getItem(new NomMachine("pikachu.uvsq.fr")).adresse());
    assertEquals(new AdresseIP("193.51.31.90"),
        relu.getItem(new NomMachine("www.uvsq.fr")).adresse());
  }

  @Test
  void addItem_nomExistant_leveDnsException() {
    DnsException e = assertThrows(DnsException.class,
        () -> dns.addItem(new AdresseIP("193.51.25.90"), new NomMachine("www.uvsq.fr")));

    assertEquals("Le nom de machine existe déjà !", e.getMessage());
  }

  @Test
  void addItem_adresseExistante_leveDnsException() {
    DnsException e = assertThrows(DnsException.class,
        () -> dns.addItem(new AdresseIP("193.51.31.90"), new NomMachine("pikachu.uvsq.fr")));

    assertEquals("L'adresse IP existe déjà !", e.getMessage());
  }

  @Test
  void addItem_refuse_fichierEtBaseInchanges() throws IOException {
    List<String> avant = Files.readAllLines(fichier);

    assertThrows(DnsException.class,
        () -> dns.addItem(new AdresseIP("193.51.31.90"), new NomMachine("pikachu.uvsq.fr")));

    assertEquals(avant, Files.readAllLines(fichier));
    assertThrows(DnsException.class, () -> dns.getItem(new NomMachine("pikachu.uvsq.fr")));
  }
}
