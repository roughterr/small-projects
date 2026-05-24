package com.roughterr;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarcheCompositeMeilleurTest {
    private static final String RGTI_TICKER = "RGTI";
    private static final Ordre RGTI_ORDRE_1 = new Ordre("1", "RGTI", Sens.ACHAT, 5, null);
    private static final Ordre RGTI_VENTE_ORDRE_1 = new Ordre("1", "RGTI", Sens.VENTE, 5, null);

    @Test
    public void shouldThrowExceptionWhenprovidedListIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new MarcheCompositeMeilleur(List.of()));
    }

    @Test
    public void shouldBuyCheaper() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(29.25d, 30.7d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.1d, 32.04d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2));
        marcheCompositeMeilleur.executerOrdre(RGTI_ORDRE_1);
        verify(m1, times(1)).executerOrdre(RGTI_ORDRE_1);
        verify(m2, never()).executerOrdre(any());
    }

    @Test
    public void shouldSellMoreExpensive() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(29.25d, 30.7d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.1d, 32.04d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2));
        marcheCompositeMeilleur.executerOrdre(RGTI_VENTE_ORDRE_1);
        verify(m1, never()).executerOrdre(any());
        verify(m2, times(1)).executerOrdre(RGTI_VENTE_ORDRE_1);
    }

    @Test
    public void shouldThrowExceptionWhenSellPriceZero() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(0d, 30.7d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(0d, 32.04d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2));
        assertThrows(IllegalStateException.class, () -> {
            marcheCompositeMeilleur.executerOrdre(RGTI_VENTE_ORDRE_1);
        });
    }

    @Test
    public void shouldThrowExceptionWhenBuyPriceZero() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(29.25d, 0d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.1d, 0d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2));
        assertThrows(IllegalStateException.class, () -> {
            marcheCompositeMeilleur.executerOrdre(RGTI_ORDRE_1);
        });
    }

    @Test
    public void shouldReturnBestPrice() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        Marche m3 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(29.25d, 30.7d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.1d, 32.04d));
        when(m3.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.2d, 30.03d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2, m3));
        PrixActuelsDuMarche prixActuelsDuMarche = marcheCompositeMeilleur.prixDuMarche(RGTI_TICKER);
        assertEquals(30.03d, prixActuelsDuMarche.achetableAUnPrix(), 0.0001);
        assertEquals(30.2d, prixActuelsDuMarche.vendableAUnPrix(), 0.0001);
    }

    @Test
    public void shouldReturnFirstOrder() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        Marche m3 = Mockito.mock(Marche.class);
        RapportExecution mockRaportExecution1 = new RapportExecution("2", RGTI_TICKER, Sens.ACHAT, 5, 12d);
        RapportExecution mockRaportExecution2 = new RapportExecution("1", RGTI_TICKER, Sens.ACHAT, 6, 12d);
        RapportExecution mockRaportExecution3 = new RapportExecution("1", RGTI_TICKER, Sens.ACHAT, 4, 10d);
        // Nous retournons un RapportExecution avec un identifiant incorrect volontairement -
        // nous voulons vérifier que notre marché composite détectera une erreur et le rejetterait.
        when(m1.recupererStatutOrdre("1")).thenReturn(mockRaportExecution1);
        when(m2.recupererStatutOrdre("1")).thenReturn(mockRaportExecution2);
        when(m3.recupererStatutOrdre("1")).thenReturn(mockRaportExecution3);
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2, m3));
        assertEquals(mockRaportExecution2, marcheCompositeMeilleur.recupererStatutOrdre("1"));
    }

    @Test
    public void shouldBuyCheaperWithLimits() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(29.25d, 30.7d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.1d, 32.04d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2));
        Ordre ordreAchatAvecLimite = new Ordre("1", "RGTI", Sens.ACHAT, 5, 35d);
        marcheCompositeMeilleur.executerOrdre(ordreAchatAvecLimite);
        verify(m1, times(1)).executerOrdre(ordreAchatAvecLimite);
        verify(m2, never()).executerOrdre(any());
    }

    @Test
    public void shouldNotBuyWhenPriceIsHigherThanBuyLimit() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(29.25d, 30.7d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.1d, 32.04d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2));
        Ordre ordreAchatAvecLimite = new Ordre("1", "RGTI", Sens.ACHAT, 5, 20d);
        marcheCompositeMeilleur.executerOrdre(ordreAchatAvecLimite);
        verify(m1, never()).executerOrdre(any());
        verify(m2, never()).executerOrdre(any());
    }

    @Test
    public void shouldNotSellWhenPriceIsHigherThanBuyLimit() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        when(m1.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(29.25d, 30.7d));
        when(m2.prixDuMarche(RGTI_TICKER)).thenReturn(new PrixActuelsDuMarche(30.1d, 32.04d));
        Marche marcheCompositeMeilleur = new MarcheCompositeMeilleur(List.of(m1, m2));
        Ordre ordreVenteAvecLimite = new Ordre("1", "RGTI", Sens.VENTE, 5, 40d);
        marcheCompositeMeilleur.executerOrdre(ordreVenteAvecLimite);
        verify(m1, never()).executerOrdre(any());
        verify(m2, never()).executerOrdre(any());
    }
}