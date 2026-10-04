package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests unitaires de la classe {@link AdresseIP}.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
class AdresseIPTest {

  @Test
  void toString_adresseValide_renvoieLaFormePointee() {
    assertEquals("193.51.31.90", new AdresseIP("193.51.31.90").toString());
  }

  @ParameterizedTest
  @ValueSource(strings = {"0.0.0.0", "255.255.255.255"})
  void constructeur_bornes_conserveLaFormePointee(String adresse) {
    assertEquals(adresse, new AdresseIP(adresse).toString());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "1.2.3", "1.2.3.4.5", "1.2.3.4.", "1..3.4", "a.b.c.d",
      "256.1.1.1", "-1.2.3.4", "+1.2.3.4", " 1.2.3.4", "01.2.3.4"})
  void constructeur_formatInvalide_leveIllegalArgument(String adresse) {
    assertThrows(IllegalArgumentException.class, () -> new AdresseIP(adresse));
  }

  @Test
  void constructeur_null_leveNullPointer() {
    assertThrows(NullPointerException.class, () -> new AdresseIP(null));
  }

  @Test
  void equals_memeAdresse_egalesEtMemeHashCode() {
    AdresseIP a1 = new AdresseIP("193.51.31.90");
    AdresseIP a2 = new AdresseIP("193.51.31.90");

    assertEquals(a1, a2);
    assertEquals(a1.hashCode(), a2.hashCode());
  }

  @Test
  void equals_adressesDifferentes_differentes() {
    assertNotEquals(new AdresseIP("193.51.31.90"), new AdresseIP("193.51.31.91"));
  }

  @Test
  void equals_autreType_faux() {
    AdresseIP adresse = new AdresseIP("193.51.31.90");

    assertFalse(adresse.equals(null));
    assertFalse(adresse.equals("193.51.31.90"));
  }

  @Test
  void compareTo_dernierOctet_ordreNumeriqueEtNonAlphabetique() {
    AdresseIP petite = new AdresseIP("193.51.31.90");
    AdresseIP grande = new AdresseIP("193.51.31.154");

    assertTrue(petite.compareTo(grande) < 0);
    assertTrue(grande.compareTo(petite) > 0);
  }

  @Test
  void compareTo_premierOctet_decideAvantLesSuivants() {
    AdresseIP petite = new AdresseIP("9.255.255.255");
    AdresseIP grande = new AdresseIP("10.0.0.0");

    assertTrue(petite.compareTo(grande) < 0);
    assertTrue(grande.compareTo(petite) > 0);
  }

  @Test
  void compareTo_memeAdresse_zero() {
    assertEquals(0, new AdresseIP("193.51.31.90").compareTo(new AdresseIP("193.51.31.90")));
  }
}
