package io.sushisquad.mangacrawler2.lib;

import java.util.List;

/**
 * A chapter which belongs to a manga, and has pages.
 */
public interface Chapter {
    /**
     * @return the {@link Series} that this chapter belongs to
     */
    Series getSeries();

    /**
     * @return the human-readable title of this chapter
     */
    String getTitle();

    /**
     * @return the list of pages in this chapter
     */
    List<Chapter> getPages();

    /**
     * @return the {@link LoadingState} this Chapter is currently in
     */
    LoadingState getState();
}
