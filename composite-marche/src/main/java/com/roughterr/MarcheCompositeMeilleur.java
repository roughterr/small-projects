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
        return null;
    }

    @Override
    public PrixActuelsDuMarche prixDuMarche(String ticker) {
        return null;
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
