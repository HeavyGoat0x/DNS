package fr.uvsq.cprog;

import java.util.Objects;

/**
 * Une entrée du DNS : une adresse IP associée à un nom qualifié de machine.
 *
 * @param adresse l'adresse de la machine
 * @param nom le nom qualifié de la machine
 */
public record DnsItem(AdresseIP adresse, NomMachine nom) {

  /**
   * Crée une entrée du DNS.
   *
   * @throws NullPointerException si l'adresse ou le nom est null
   */
  public DnsItem {
    Objects.requireNonNull(adresse, "L'adresse ne peut pas être null");
    Objects.requireNonNull(nom, "Le nom ne peut pas être null");
  }

  /**
   * Renvoie l'entrée sous la forme affichée par {@code ls}.
   *
   * @return l'adresse puis le nom, séparés par une espace, par exemple
   *     {@code 193.51.31.90 www.uvsq.fr}
   */
  @Override
  public String toString() {
    return adresse + " " + nom;
  }
}
