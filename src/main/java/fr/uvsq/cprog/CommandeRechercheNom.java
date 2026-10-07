package fr.uvsq.cprog;

/**
 * Recherche le nom qualifié d'une machine à partir de son adresse IP.
 *
 * <p>Exemple : {@code 193.51.31.90} affiche {@code www.uvsq.fr}.
 */
public class CommandeRechercheNom implements Commande {

  private final Dns dns;
  private final AdresseIP adresse;

  /**
   * Crée la commande.
   *
   * @param dns le DNS à interroger
   * @param adresse l'adresse IP de la machine dont on cherche le nom
   */
  public CommandeRechercheNom(Dns dns, AdresseIP adresse) {
    this.dns = dns;
    this.adresse = adresse;
  }

  @Override
  public String execute() throws DnsException {
    return dns.getItem(adresse).nom().toString();
  }
}
