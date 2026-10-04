package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests unitaires de la classe {@link NomMachine}.
 */
class NomMachineTest {

  @Test
  void getMachine_nomQualifie_renvoieAvantLePremierPoint() {
    assertEquals("www", new NomMachine("www.uvsq.fr").getMachine());
  }

  @Test
  void getDomaine_nomQualifie_renvoieApresLePremierPoint() {
    assertEquals("uvsq.fr", new NomMachine("www.uvsq.fr").getDomaine());
  }

  @Test
  void getDomaine_plusieursPoints_gardeToutApresLePremier() {
    assertEquals("prism.uvsq.fr", new NomMachine("persee.prism.uvsq.fr").getDomaine());
  }

  @Test
  void toString_nomQualifie_renvoieLeNomComplet() {
    assertEquals("www.uvsq.fr", new NomMachine("www.uvsq.fr").toString());
  }

  @ParameterizedTest
  @ValueSource(strings = {"a.b", "www2.uvsq.fr", "mon-poste.uvsq.fr"})
  void constructeur_nomValide_conserveLeNom(String nom) {
    assertEquals(nom, new NomMachine(nom).toString());
  }

  @Test
  void constructeur_majuscules_convertiesEnMinuscules() {
    NomMachine nom = new NomMachine("Www.UVSQ.Fr");

    assertEquals("www.uvsq.fr", nom.toString());
    assertEquals("www", nom.getMachine());
    assertEquals("uvsq.fr", nom.getDomaine());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "www", ".uvsq.fr", "www.", "www..fr", "www uvsq.fr",
      "-www.uvsq.fr", "www-.uvsq.fr", "www_1.uvsq.fr"})
  void constructeur_nomInvalide_leveIllegalArgument(String nom) {
    assertThrows(IllegalArgumentException.class, () -> new NomMachine(nom));
  }

  @Test
  void constructeur_null_leveNullPointer() {
    assertThrows(NullPointerException.class, () -> new NomMachine(null));
  }
}
