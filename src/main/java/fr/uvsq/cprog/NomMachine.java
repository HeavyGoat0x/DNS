package fr.uvsq.cprog;

/**
 * Un nom qualifié de machine, par exemple {@code www.uvsq.fr}.
 *
 * <p>Le nom de la machine est la partie avant le premier point ({@code www}), le nom de domaine
 * la partie après ({@code uvsq.fr}).
 */
public class NomMachine {

  private final String nom;

  /**
   * Crée un nom qualifié de machine.
   *
   * @param nom le nom qualifié, sous la forme {@code machine.domaine}
   */
  public NomMachine(String nom) {
    this.nom = nom;
  }

  /**
   * Renvoie le nom de la machine, sans le domaine.
   *
   * @return la partie avant le premier point, par exemple {@code www}
   */
  public String getMachine() {
    return nom.substring(0, nom.indexOf('.'));
  }

  /**
   * Renvoie le nom de domaine de la machine.
   *
   * @return la partie après le premier point, par exemple {@code uvsq.fr}
   */
  public String getDomaine() {
    return nom.substring(nom.indexOf('.') + 1);
  }

  @Override
  public String toString() {
    return nom;
  }
}
