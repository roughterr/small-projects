package com.roughterr.cache;

import com.roughterr.Marche;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MarcheCache {
    private AtomicInteger quantiteDisponible;
    private Marche marche;
    private double vendablePrix;
    private double achetablePrix;

    public MarcheCache(int quantiteDisponible, Marche marche, double vendablePrix, double achetablePrix) {
        this.quantiteDisponible = new AtomicInteger(quantiteDisponible);
        this.marche = marche;
        this.vendablePrix = vendablePrix;
        this.achetablePrix = achetablePrix;
    }

    public int getQuantiteDisponible() {
        return quantiteDisponible.get();
    }

    public Marche getMarche() {
        return marche;
    }

    public double getVendablePrix() {
        return vendablePrix;
    }

    public double getAchetablePrix() {
        return achetablePrix;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MarcheCache that = (MarcheCache) o;
        return Objects.equals(marche, that.marche);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(marche);
    }
}
