package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de la classe {@link AdresseIP}.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
class AdresseIPTest {

  @Test
  void toString_adresseValide_renvoieLaFormePointee() {
    assertEquals("193.51.31.90", new AdresseIP("193.51.31.90").toString());
  }
}
