package fr.uvsq.cprog;

/**
 * Recherche l'adresse IP d'une machine à partir de son nom qualifié.
 *
 * <p>Exemple : {@code www.uvsq.fr} affiche {@code 193.51.31.90}.
 */
public class CommandeRechercheAdresse implements Commande {

  private final Dns dns;
  private final NomMachine nom;

  /**
   * Crée la commande.
   *
   * @param dns le DNS à interroger
   * @param nom le nom qualifié de la machine dont on cherche l'adresse
   */
  public CommandeRechercheAdresse(Dns dns, NomMachine nom) {
    this.dns = dns;
    this.nom = nom;
  }

  @Override
  public String execute() throws DnsException {
    return dns.getItem(nom).adresse().toString();
  }
}
