package fr.uvsq.cprog;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Un nom qualifié de machine, par exemple {@code www.uvsq.fr}.
 *
 * <p>Le nom de la machine est la partie avant le premier point ({@code www}), le nom de domaine
 * la partie après ({@code uvsq.fr}).
 *
 * <p>Comme dans le DNS, la casse est ignorée : le nom est conservé en minuscules.
 */
public class NomMachine {
  /** Un label : lettres, chiffres et tirets, sans tiret au début ni à la fin. */
  private static final String LABEL = "[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?";
  private static final Pattern FORMAT = Pattern.compile(LABEL + "(\\." + LABEL + ")+");

  private final String nom;

  /**
   * Crée un nom qualifié de machine, converti en minuscules.
   *
   * @param nom le nom qualifié, sous la forme {@code machine.domaine}
   * @throws NullPointerException si le nom est null
   * @throws IllegalArgumentException si le nom n'a pas la forme {@code machine.domaine}
   */
  public NomMachine(String nom) {
    Objects.requireNonNull(nom, "Un nom de machine ne peut pas être null");

    if (!FORMAT.matcher(nom).matches()) {
      throw new IllegalArgumentException("Nom de machine invalide : " + nom);
    }
    this.nom = nom.toLowerCase(Locale.ROOT);
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
