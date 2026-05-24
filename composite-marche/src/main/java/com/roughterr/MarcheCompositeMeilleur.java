package com.roughterr;

import java.util.List;

public class MarcheCompositeMeilleur implements Marche {
    private List<Marche> marches;

    public MarcheCompositeMeilleur(List<Marche> marches) {
        if (marches == null || marches.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.marches = marches;
    }

    @Override
    public RapportExecution executerOrdre(Ordre ordre) {
        if (ordre.sens() == Sens.ACHAT) {
            double prixAchatMinimum = Double.MAX_VALUE;
            Marche marcheAvecPrixAchatMinimum = null;
            for (Marche marche : marches) {
                PrixActuelsDuMarche prixActuelsDuMarche = marche.prixDuMarche(ordre.ticker());
                if (prixActuelsDuMarche.achetableAUnPrix() < prixAchatMinimum) {
                    prixAchatMinimum = prixActuelsDuMarche.achetableAUnPrix();
                    marcheAvecPrixAchatMinimum = marche;
                }
            }
            if (prixAchatMinimum == 0d) {
                throw new IllegalStateException();
            }
            if (marcheAvecPrixAchatMinimum != null && (ordre.prixLimite() == null || ordre.prixLimite() >= prixAchatMinimum)){
                return marcheAvecPrixAchatMinimum.executerOrdre(ordre);
            }
        } else if (ordre.sens() == Sens.VENTE) {
            double prixVenteMaximum = 0d;
            Marche marcheAvecPrixVenteMaximum = null;
            for (Marche marche : marches) {
                PrixActuelsDuMarche prixActuelsDuMarche = marche.prixDuMarche(ordre.ticker());
                if (prixActuelsDuMarche.vendableAUnPrix() > prixVenteMaximum) {
                    prixVenteMaximum = prixActuelsDuMarche.vendableAUnPrix();
                    marcheAvecPrixVenteMaximum = marche;
                }
            }
            if (prixVenteMaximum == 0d) {
                throw new IllegalStateException();
            }
            // Vérifiez que les limites ne sont pas dépassées
            if (ordre.prixLimite() == null || ordre.prixLimite() <= prixVenteMaximum) {
                return marcheAvecPrixVenteMaximum.executerOrdre(ordre);
            }
        }
        return null;
    }

    @Override
    public PrixActuelsDuMarche prixDuMarche(String ticker) {
        double prixAchatMinimum = Double.MAX_VALUE;
        double prixVenteMaximum = 0d;
        for (Marche marche : marches) {
            PrixActuelsDuMarche prixActuelsDuMarche = marche.prixDuMarche(ticker);
            if (prixActuelsDuMarche.achetableAUnPrix() < prixAchatMinimum) {
                prixAchatMinimum = prixActuelsDuMarche.achetableAUnPrix();
            }
            if (prixActuelsDuMarche.vendableAUnPrix() > prixVenteMaximum) {
                prixVenteMaximum = prixActuelsDuMarche.vendableAUnPrix();
            }
        }
        return new PrixActuelsDuMarche(prixVenteMaximum, prixAchatMinimum);
    }

    @Override
    public RapportExecution recupererStatutOrdre(String identifiantOrdreClient) {
        for (Marche marche : marches) {
            RapportExecution rapportExecution = marche.recupererStatutOrdre(identifiantOrdreClient);
            if (rapportExecution != null && identifiantOrdreClient.equals(rapportExecution.identifiantOrdreClient())) {
                return rapportExecution;
            }
        }
        return null;
    }
}
