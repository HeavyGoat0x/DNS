package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
}
