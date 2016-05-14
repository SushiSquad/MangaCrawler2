package io.sushisquad.mangacrawler2.lib;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class SeriesBase implements Series {
    /**
     * AtomicReference to the current state. This is an AtomicReference so that
     * we can safely update it from multiple threads.
     */
    private final AtomicReference<LoadingState> state = new AtomicReference<>(LoadingState.NOT_LOADED);

    /**
     * Internal, mutable list of {@link Chapter} which {@link #loadChapters(List)}
     * updates.
     */
    private final ArrayList<Chapter> chapters = new ArrayList<>(100);

    /**
     * Abstract method which subclasses implement with their chapters loading
     * logic.
     *
     * @param chapterList List of {@link Chapter} which the subclass updates as
     * it loads chapters.
     */
    protected abstract void loadChapters(List<Chapter> chapterList) throws Exception;

    public abstract URI getImageURI();

    @Override
    public List<Chapter> getChapters() {
        // If we're in the NOT_LOADED state we need to start loading manga
        if (state.get() == LoadingState.NOT_LOADED) {
            // In a new thread, load chapters
            MangaCrawler2.threadPool.submit(() -> {
                try {
                    state.set(LoadingState.LOADING);
                    loadChapters(chapters);
                    state.set(LoadingState.LOADED);
                } catch (Throwable t) {
                    state.set(LoadingState.LOAD_FAILED);
                }
            });

            // State may be set to LOADING before now, so we must only set
            // WAITING if we're in the NOT_LOADED state
            state.compareAndSet(LoadingState.NOT_LOADED, LoadingState.WAITING);
        }

        // Return an unmodifiable view of the list so callers cannot modify chapters
        return Collections.unmodifiableList(chapters);
    }

    @Override
    public LoadingState getState() {
        return state.get();
    }
    @Override
    public InputStream getImageData() throws IOException {
        HttpResponse response = MangaCrawler2.httpClient.execute(new HttpGet(getImageURI()));
        return response.getEntity().getContent();
    }
}
