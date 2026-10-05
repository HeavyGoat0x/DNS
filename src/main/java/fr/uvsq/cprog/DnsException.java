package fr.uvsq.cprog;

/**
 * Une erreur du DNS : entrée inconnue, doublon ou base illisible.
 *
 * <p>C'est une exception vérifiée : l'appelant doit prévoir ces cas, qui font partie du
 * fonctionnement normal du DNS.
 */
public class DnsException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Crée une erreur du DNS.
   *
   * @param message la description de l'erreur, destinée à l'utilisateur
   */
  public DnsException(String message) {
    super(message);
  }

  /**
   * Crée une erreur du DNS provoquée par une autre exception.
   *
   * @param message la description de l'erreur, destinée à l'utilisateur
   * @param cause l'exception d'origine, par exemple une {@link java.io.IOException}
   */
  public DnsException(String message, Throwable cause) {
    super(message, cause);
  }
}
