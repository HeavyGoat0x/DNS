package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Un serveur DNS simulé : associe des noms qualifiés de machines à des adresses IP.
 *
 * <p>La base est un fichier texte, à raison d'une machine par ligne au format
 * {@code nom.qualifie adresse.ip}. Elle est chargée en mémoire à la création, puis indexée deux
 * fois pour permettre la recherche dans les deux sens : par nom et par adresse.
 */
public class Dns {

  private final Path fichier;

  /**
   * Les entrées indexées par adresse, dans l'ordre du fichier.
   */
  private final Map<AdresseIP, DnsItem> parAdresse = new LinkedHashMap<>();

  /**
   * Les mêmes entrées, indexées par nom.
   */
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
   * @return l'entrée correspondante
   * @throws DnsException si le nom est inconnu
   */
  public DnsItem getItem(NomMachine nom) throws DnsException {
    DnsItem item = parNom.get(nom);
    if (item == null) {
      throw new DnsException("Machine inconnue : " + nom);
    }
    return item;
  }

  /**
   * Recherche une machine par son adresse IP.
   *
   * @param adresse l'adresse IP de la machine
   * @return l'entrée correspondante
   * @throws DnsException si l'adresse est inconnue
   */
  public DnsItem getItem(AdresseIP adresse) throws DnsException {
    DnsItem item = parAdresse.get(adresse);
    if (item == null) {
      throw new DnsException("Adresse inconnue : " + adresse);
    }
    return item;
  }

  /**
   * Renvoie les machines d'un domaine, sans tenir compte de la casse.
   *
   * @param domaine le nom de domaine, par exemple {@code uvsq.fr}
   * @return les entrées dont le nom appartient à ce domaine, sans ordre garanti ; une liste vide
   *     si le domaine n'a aucune machine
   */
  public List<DnsItem> getItems(String domaine) {
    String domaineMinuscules = domaine.toLowerCase(Locale.ROOT);
    return parAdresse.values().stream()
        .filter(item -> item.nom().getDomaine().equals(domaineMinuscules))
        .toList();
  }
}
