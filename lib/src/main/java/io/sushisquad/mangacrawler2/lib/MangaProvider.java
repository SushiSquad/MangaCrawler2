package io.sushisquad.mangacrawler2.lib;

import java.util.List;

/**
 * A source of manga e.g. Batoto, MangaHere, KissManga.
 */
public interface MangaProvider {
    /**
     * @return the human-readable name of this manga provider, e.g. Batoto,
     *         MangaHere, KissManga.
     */
    String getName();

    /**
     * @return the list of series available through this provider
     */
    List<Series> getSeries();

    /**
     * @return the {@link LoadingState} this MangaProvider is currently in
     */
    LoadingState getState();
}
