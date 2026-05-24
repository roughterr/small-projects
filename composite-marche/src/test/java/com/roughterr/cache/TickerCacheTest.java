package com.roughterr.cache;

import com.roughterr.Marche;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class TickerCacheTest {

    @Test
    void setDateMiseEnCache() {
        TickerCache tickerCache = new TickerCache();
        TickerCache spyTickerCache = spy(tickerCache);
        // 5 min de la différence
        when(spyTickerCache.maintenant()).thenReturn(Instant.parse("1980-04-09T15:35:45.123Z"));
        spyTickerCache.setDateMiseEnCache(Instant.parse("1980-04-09T15:30:45.123Z"));
        // durée 1 min
        assertTrue(spyTickerCache.estExpire(Duration.ofMinutes(1)));
        assertFalse(spyTickerCache.estExpire(Duration.ofMinutes(10)));
    }

    @Test
    void ajouterMarche() {
        Marche m1 = Mockito.mock(Marche.class);
        Marche m2 = Mockito.mock(Marche.class);
        Marche m3 = Mockito.mock(Marche.class);
        MarcheCache m1Cache = new MarcheCache(10,  m1, 10, 8);
        MarcheCache m2Cache = new MarcheCache(10,  m2, 12, 10);
        MarcheCache m3Cache = new MarcheCache(10,  m3, 10, 5);
        TickerCache tickerCache = new TickerCache();
        tickerCache.ajouterMarche(m1Cache);
        tickerCache.ajouterMarche(m2Cache);
        tickerCache.ajouterMarche(m3Cache);
        assertEquals(m3Cache, tickerCache.getMarchesTriesParPrixAchat().getFirst());
        assertEquals(m2Cache, tickerCache.getMarchesTriesParPrixVente().getLast());
    }
}