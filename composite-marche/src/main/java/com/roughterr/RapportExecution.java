package com.roughterr;

/**
 * Ce record représente un rapport du marché après avoir tenté d'exécuter notre ordre.
 * @param identifiantOrdreClient le même identifiantOrdreClient que sur l'ordre
 * @param ticker le même ticker que sur l'ordre
 * @param sens le même Sens que sur l'ordre
 * @param nombreActions c'est la quantité réellement exécutée; elle peut être inférieure ou égale à la quantité de l'ordre
 * @param prix prix auquel l'ordre a été exécuté
 */
public record RapportExecution(String identifiantOrdreClient, String ticker, Sens sens, int nombreActions, double prix) {
}
