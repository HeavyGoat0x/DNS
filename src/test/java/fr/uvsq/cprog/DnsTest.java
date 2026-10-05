package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests unitaires de la classe {@link Dns}.
 */
class DnsTest {

  @TempDir
  Path dossier;

  private Dns dns;

  @BeforeEach
  void setUp() throws IOException, DnsException {
    dns = new Dns(BaseExemple.ecrit(dossier));
  }

  @Test
  void getItem_nomConnu_renvoieSonAdresse() {
    DnsItem item = dns.getItem(new NomMachine("www.uvsq.fr"));

    assertEquals(new AdresseIP("193.51.31.90"), item.adresse());
  }

  @Test
  void getItem_adresseConnue_renvoieSonNom() {
    DnsItem item = dns.getItem(new AdresseIP("193.51.31.90"));

    assertEquals(new NomMachine("www.uvsq.fr"), item.nom());
  }

  @Test
  void getItem_toutesLesLignes_chargees() {
    assertEquals(new NomMachine("ecampus.uvsq.fr"),
        dns.getItem(new AdresseIP("193.51.25.12")).nom());
    assertEquals(new AdresseIP("10.0.0.1"),
        dns.getItem(new NomMachine("mail.exemple.org")).adresse());
  }
}
