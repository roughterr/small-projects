package com.roughterr;

/**
 * Cet enregistrement représente un ordre que nous pouvons envoyer à un marché.
 * Lors de l'envoi d'un ordre, nous devons spécifier:
 *
 * @param identifiantOrdreClient un identifiant que vous avez attribué à l'ordre
 * @param ticker                 il s'agit du "nom" sous forme de chaîne d'une action spécifique que nous voulons acheter.
 *                               Exemples : "NVDA" pour Nvidia, "META" pour Meta, "ORCL" pour Oracle, etc.
 * @param sens                   ACHAT ou VENTE indiquant si nous voulons acheter ou vendre le ticker
 * @param nombreActions          nombre d'actions que nous voulons acheter ou vendre
 */
public record Ordre(String identifiantOrdreClient, String ticker, Sens sens, int nombreActions) {
}
