package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
