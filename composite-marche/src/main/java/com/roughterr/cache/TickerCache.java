package com.roughterr.cache;

import com.roughterr.Marche;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Contient un cache des prix des actions d'une entreprise sur différents marchés.
 */
public class TickerCache {
    private Instant dateMiseEnCache = Instant.now();
    private TreeSet<MarcheCache> marchesTriesParPrixAchat = new TreeSet<>(Comparator.comparing(MarcheCache::getAchetablePrix));
    private TreeSet<MarcheCache> marchesTriesParPrixVente = new TreeSet<>(Comparator.comparing(MarcheCache::getVendablePrix));

    public boolean estExpire(Duration ttl) {
        return dateMiseEnCache.plus(ttl).isBefore(maintenant());
    }

    /**
     * Cette méthode sert surtout pour les tests.
     */
    protected Instant maintenant() {
        return Instant.now();
    }

    /**
     * Cette méthode sert surtout pour les tests.
     */
    protected void setDateMiseEnCache(Instant dateMiseEnCache) {
        this.dateMiseEnCache = dateMiseEnCache;
    }

    public void ajouterMarche(MarcheCache marcheCache) {
        marchesTriesParPrixAchat.add(marcheCache);
        marchesTriesParPrixVente.add(marcheCache);
    }

    public TreeSet<MarcheCache> getMarchesTriesParPrixAchat() {
        return marchesTriesParPrixAchat;
    }

    public TreeSet<MarcheCache> getMarchesTriesParPrixVente() {
        return marchesTriesParPrixVente;
    }
}
