package io.sushisquad.mangacrawler2.lib;

import java.io.OutputStream;
import java.util.List;

/**
 * A manga series, from a specific manga provider, which contains chapters.
 */
public interface Series {
    /**
     * @return the {@link MangaProvider} that provides this series
     */
    MangaProvider getProvider();

    /**
     * @return the human-readable title of this manga series
     */
    String getTitle();

    /**
     * @return the description of this manga series
     */
    String getDescription();

    /**
     * @return a stream of image data for the preview image for this Series
     */
    OutputStream getImageData();

    /**
     * @return the list of chapters for this series
     */
    List<Chapter> getChapters();

    /**
     * @return the {@link LoadingState} this Series is currently in.
     */
    LoadingState getState();
}
