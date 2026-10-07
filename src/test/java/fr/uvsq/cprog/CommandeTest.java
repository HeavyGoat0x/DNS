package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests unitaires des commandes du DNS.
 */
class CommandeTest {

  @TempDir
  Path dossier;

  private Dns dns;

  @BeforeEach
  void setUp() throws IOException, DnsException {
    dns = new Dns(BaseExemple.ecrit(dossier));
  }

  @Test
  void rechercheAdresse_nomConnu_renvoieAdresse() throws DnsException {
    Commande commande = new CommandeRechercheAdresse(dns, new NomMachine("www.uvsq.fr"));

    assertEquals("193.51.31.90", commande.execute());
  }

  @Test
  void rechercheAdresse_nomInconnu_leveDnsException() {
    Commande commande = new CommandeRechercheAdresse(dns, new NomMachine("inconnu.uvsq.fr"));

    assertThrows(DnsException.class, commande::execute);
  }

  @Test
  void rechercheNom_adresseConnue_renvoieNom() throws DnsException {
    Commande commande = new CommandeRechercheNom(dns, new AdresseIP("193.51.31.90"));

    assertEquals("www.uvsq.fr", commande.execute());
  }

  @Test
  void rechercheNom_adresseInconnue_leveDnsException() {
    Commande commande = new CommandeRechercheNom(dns, new AdresseIP("1.2.3.4"));

    assertThrows(DnsException.class, commande::execute);
  }

  @Test
  void listeDomaine_sansOption_trieParNom() throws DnsException {
    Commande commande = new CommandeListeDomaine(dns, "uvsq.fr", false);

    assertEquals("193.51.25.12 ecampus.uvsq.fr\n"
        + "193.51.31.154 poste.uvsq.fr\n"
        + "193.51.31.90 www.uvsq.fr", commande.execute());
  }

  @Test
  void listeDomaine_optionA_trieParAdresseNumerique() throws DnsException {
    dns.addItem(new AdresseIP("193.51.25.24"), new NomMachine("pikachu.uvsq.fr"));
    Commande commande = new CommandeListeDomaine(dns, "uvsq.fr", true);

    assertEquals("193.51.25.12 ecampus.uvsq.fr\n"
        + "193.51.25.24 pikachu.uvsq.fr\n"
        + "193.51.31.90 www.uvsq.fr\n"
        + "193.51.31.154 poste.uvsq.fr", commande.execute());
  }

  @Test
  void listeDomaine_domaineInconnu_chaineVide() {
    assertEquals("", new CommandeListeDomaine(dns, "inconnu.org", false).execute());
  }
}
