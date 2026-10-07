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
}
