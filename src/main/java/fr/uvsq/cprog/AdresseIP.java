package fr.uvsq.cprog;

/**
 * Une adresse IPv4.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class AdresseIP {
  private final String adresse;

  /**
   * Crée une adresse IP à partir de sa forme pointée.
   *
   * @param adresse l'adresse sous la forme {@code a.b.c.d}
   */
  public AdresseIP(String adresse) {
    this.adresse = adresse;
  }

  @Override
  public String toString() {
    return adresse;
  }
}
