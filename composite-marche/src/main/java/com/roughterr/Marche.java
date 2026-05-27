package com.roughterr;

import java.util.Optional;

public interface Marche {
    /**
     * Envoie un ordre à un marché pour exécution.
     */
    RapportExecution executerOrdre(Ordre ordre);

    /**
     * Retourne les prix actuels auxquels vous pouvez acheter ou vendre un "ticker"
     * (voir Order ci-dessous pour l'explication).
     */
    PrixActuelsDuMarche prixDuMarche(String ticker);

    /**
     * Récupère le statut d'un ordre via son identifiantOrdreClient après son exécution.
     */
    RapportExecution recupererStatutOrdre(String identifiantOrdreClient);
}
