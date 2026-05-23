package com.roughterr;

/**
 * Ce record représente les prix actuels auxquels nous pouvons acheter et vendre certaines actions spécifiques sur ce marché.
 * @param vendableAUnPrix prix attendu si nous envoyons un ordre avec le side=Sell; uniquement applicable si vendableAUnPrix > 0
 * @param achetableAUnPrix prix attendu si nous envoyons un ordre avec side=Buy; uniquement applicable si achetableAUnPrix > 0
 */
public record PrixActuelsDuMarche(double vendableAUnPrix, double achetableAUnPrix) {
}
