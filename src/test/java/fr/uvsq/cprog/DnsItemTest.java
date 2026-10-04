package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la classe {@link DnsItem}.
 */
class DnsItemTest {

  private final AdresseIP adresse = new AdresseIP("193.51.31.90");
  private final NomMachine nom = new NomMachine("www.uvsq.fr");

  @Test
  void toString_item_adressePuisNom() {
    assertEquals("193.51.31.90 www.uvsq.fr", new DnsItem(adresse, nom).toString());
  }

  @Test
  void accesseurs_item_renvoientAdresseEtNom() {
    DnsItem item = new DnsItem(adresse, nom);

    assertEquals(adresse, item.adresse());
    assertEquals(nom, item.nom());
  }

  @Test
  void equals_memesComposants_egauxEtMemeHashCode() {
    DnsItem item1 = new DnsItem(new AdresseIP("193.51.31.90"), new NomMachine("www.uvsq.fr"));
    DnsItem item2 = new DnsItem(new AdresseIP("193.51.31.90"), new NomMachine("www.uvsq.fr"));

    assertEquals(item1, item2);
    assertEquals(item1.hashCode(), item2.hashCode());
  }

  @Test
  void equals_composantsDifferents_differents() {
    DnsItem item = new DnsItem(adresse, nom);

    assertNotEquals(item, new DnsItem(new AdresseIP("193.51.31.91"), nom));
    assertNotEquals(item, new DnsItem(adresse, new NomMachine("poste.uvsq.fr")));
  }

  @Test
  void constructeur_adresseNull_leveNullPointer() {
    assertThrows(NullPointerException.class, () -> new DnsItem(null, nom));
  }

  @Test
  void constructeur_nomNull_leveNullPointer() {
    assertThrows(NullPointerException.class, () -> new DnsItem(adresse, null));
  }
}
