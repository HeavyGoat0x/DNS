package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Un serveur DNS simulé : associe des noms qualifiés de machines à des adresses IP.
 *
 * <p>La base est un fichier texte, à raison d'une machine par ligne au format
 * {@code nom.qualifie adresse.ip}. Elle est chargée en mémoire à la création, puis indexée
 * deux fois pour permettre la recherche dans les deux sens : par nom et par adresse.
 */
public class Dns {

  private final Path fichier;

  /** Les entrées indexées par adresse, dans l'ordre du fichier. */
  private final Map<AdresseIP, DnsItem> parAdresse = new LinkedHashMap<>();

  /** Les mêmes entrées, indexées par nom. */
  private final Map<NomMachine, DnsItem> parNom = new HashMap<>();

  /**
   * Charge la base du DNS depuis un fichier texte.
   *
   * @param fichier le fichier de la base, une machine par ligne au format
   *     {@code nom.qualifie adresse.ip}
   * @throws DnsException si le fichier ne peut pas être lu
   */
  public Dns(Path fichier) throws DnsException {
    this.fichier = fichier;

    List<String> lignes;
    try {
      lignes = Files.readAllLines(fichier);
    } catch (IOException e) {
      throw new DnsException("Lecture impossible : " + fichier, e);
    }

    for (String ligne : lignes) {
      if (ligne.isBlank()) {
        continue;
      }

      String[] champs = ligne.trim().split("\\s+");
      AdresseIP adresse = new AdresseIP(champs[1]);
      NomMachine nom = new NomMachine(champs[0]);
      DnsItem dnsItem = new DnsItem(adresse, nom);
      parAdresse.put(adresse, dnsItem);
      parNom.put(nom, dnsItem);
    }
  }

  /**
   * Recherche une machine par son nom qualifié.
   *
   * @param nom le nom qualifié de la machine
   * @return l'entrée correspondante, ou {@code null} si le nom est inconnu
   */
  public DnsItem getItem(NomMachine nom) {
    return parNom.get(nom);
  }

  /**
   * Recherche une machine par son adresse IP.
   *
   * @param adresse l'adresse IP de la machine
   * @return l'entrée correspondante, ou {@code null} si l'adresse est inconnue
   */
  public DnsItem getItem(AdresseIP adresse) {
    return parAdresse.get(adresse);
  }
}
