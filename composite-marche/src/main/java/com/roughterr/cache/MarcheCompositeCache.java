package com.roughterr.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache des prix par ticker.
 * La clé correspond au ticker (identifiant de l'entreprise),
 * la valeur contient les prix mis en cache sur différents marchés.
 */
public class MarcheCompositeCache {
    private Map<String, TickerCache> cache = new HashMap<>();
    /**
     * Time to Live.
     */
    private Duration ttl;
}
