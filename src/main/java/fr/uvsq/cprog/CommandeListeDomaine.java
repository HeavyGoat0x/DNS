package fr.uvsq.cprog;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Liste les machines d'un domaine, triées par nom ou par adresse : commande {@code ls [-a]}.
 *
 * <p>Chaque machine est affichée sur une ligne, au format {@code adresse nom}.
 */
public class CommandeListeDomaine implements Commande {

  private final Dns dns;
  private final String domaine;
  private final boolean triParAdresse;

  /**
   * Crée la commande.
   *
   * @param dns le DNS à interroger
   * @param domaine le domaine dont on liste les machines, par exemple {@code uvsq.fr}
   * @param triParAdresse {@code true} pour trier par adresse IP (option {@code -a}),
   *     {@code false} pour trier par nom
   */
  public CommandeListeDomaine(Dns dns, String domaine, boolean triParAdresse) {
    this.dns = dns;
    this.domaine = domaine;
    this.triParAdresse = triParAdresse;
  }

  @Override
  public String execute() {
    Comparator<DnsItem> ordre = triParAdresse
        ? Comparator.comparing(DnsItem::adresse)
        : Comparator.comparing(DnsItem::nom);

    return dns.getItems(domaine).stream()
        .sorted(ordre)
        .map(DnsItem::toString)
        .collect(Collectors.joining("\n"));
  }
}
