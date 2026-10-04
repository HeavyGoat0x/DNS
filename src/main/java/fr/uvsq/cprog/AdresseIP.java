package fr.uvsq.cprog;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Une adresse IPv4, par exemple {@code 193.51.31.90}.
 *
 * <p>Une instance est toujours valide et ne change jamais : elle peut servir de clé dans une
 * table. Deux adresses sont égales si leurs quatre octets le sont, et elles sont ordonnées
 * numériquement, octet par octet ({@code 193.51.31.90} avant {@code 193.51.31.154}).
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class AdresseIP implements Comparable<AdresseIP> {
  private static final int NB_OCTETS = 4;
  private static final int OCTET_MAX = 255;

  /** Un octet en décimal : 0, ou un nombre sans zéro en tête ni signe. */
  private static final String OCTET = "(0|[1-9]\\d{0,2})";
  private static final Pattern FORMAT =
      Pattern.compile(String.join("\\.", Collections.nCopies(NB_OCTETS, OCTET)));

  private final int[] octets = new int[NB_OCTETS];

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

    for (int i = 0; i < NB_OCTETS; i++) {
      int octet = Integer.parseInt(morceaux.group(i + 1));
      if (octet > OCTET_MAX) {
        throw new IllegalArgumentException(
            "Chaque octet doit être entre 0 et " + OCTET_MAX + " : " + adresse);
      }
      octets[i] = octet;
    }
  }

  @Override
  public String toString() {
    return Arrays.stream(octets)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining("."));
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof AdresseIP autre && Arrays.equals(octets, autre.octets);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(octets);
  }

  @Override
  public int compareTo(AdresseIP autre) {
    return Arrays.compare(octets, autre.octets);
  }
}
