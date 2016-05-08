package io.sushisquad.mangacrawler2.lib;

import java.io.OutputStream;

/**
 * A page of a manga which can return image data.
 */
public interface Page {
    /**
     * @return the Chapter that this Page belongs to
     */
    Chapter getChapter();

    /**
     * @return a stream of the image data of this page
     */
    OutputStream getImageData();

    /**
     * @return the {@link PageState} this Page is currently in
     */
    PageState getState();

    enum PageState {
        /**
         * This Page isn't downloaded or ready for viewing.
         */
        NOT_DOWNLOADED,

        /**
         * This Page is waiting in the download queue.
         */
        WAITING,

        /**
         * This page is currently being downloaded.
         */
        DOWNLOADING,

        /**
         * THis page is currently saved on disk and ready for viewing.
         */
        SAVED,

        /**
         * This page failed while downloading and cannot be viewed.
         */
        DOWNLOAD_FAILED
    }
}
