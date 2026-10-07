package fr.uvsq.cprog;

/**
 * Ajoute une machine au DNS : commande {@code add adresse nom}.
 *
 * <p>Comme dans le sujet, un ajout réussi n'affiche rien. En cas de doublon, l'erreur remonte
 * sous la forme d'une {@link DnsException}.
 */
public class CommandeAjout implements Commande {

  private final Dns dns;
  private final AdresseIP adresse;
  private final NomMachine nom;

  /**
   * Crée la commande.
   *
   * @param dns le DNS à modifier
   * @param adresse l'adresse IP de la nouvelle machine
   * @param nom le nom qualifié de la nouvelle machine
   */
  public CommandeAjout(Dns dns, AdresseIP adresse, NomMachine nom) {
    this.dns = dns;
    this.adresse = adresse;
    this.nom = nom;
  }

  @Override
  public String execute() throws DnsException {
    dns.addItem(adresse, nom);
    return "";
  }
}
