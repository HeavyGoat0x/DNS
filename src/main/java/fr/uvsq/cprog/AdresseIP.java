package fr.uvsq.cprog;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Une adresse IPv4.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class AdresseIP {
  private static final String OCTET = "(0|[1-9]\\d{0,2})";
  private static final Pattern FORMAT =
      Pattern.compile(OCTET + "\\." + OCTET + "\\." + OCTET + "\\." + OCTET);

  private final int[] octets = new int[4];

  /**
   * Crée une adresse IP à partir de sa forme pointée.
   *
   * @param adresse l'adresse sous la forme {@code a.b.c.d}
   * @throws NullPointerException si l'adresse est null
   * @throws IllegalArgumentException si l'adresse n'est pas une adresse IPv4 valide
   */
  public AdresseIP(String adresse) {
    Objects.requireNonNull(adresse, "Une adresse ne peut pas être null");

    Matcher morceaux = FORMAT.matcher(adresse);
    if (!morceaux.matches()) {
      throw new IllegalArgumentException("Adresse IP invalide : " + adresse);
    }

    for (int i = 0; i < 4; i++) {
      int val = Integer.parseInt(morceaux.group(i + 1));
      if (val > 255) {
        throw new IllegalArgumentException(
            "Chaque octet doit être entre 0 et 255 : " + adresse);
      }
      octets[i] = val;
    }
  }

  @Override
  public String toString() {
    return String.format("%d.%d.%d.%d", octets[0], octets[1], octets[2], octets[3]);
  }
}
