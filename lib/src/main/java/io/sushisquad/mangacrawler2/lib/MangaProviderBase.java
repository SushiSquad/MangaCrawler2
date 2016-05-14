package io.sushisquad.mangacrawler2.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base class that deals with updating the state machine
 */
public abstract class MangaProviderBase implements MangaProvider {
    /**
     * AtomicReference to the current state. This is an AtomicReference so that
     * we can safely update it from multiple threads.
     */
    private final AtomicReference<LoadingState> state = new AtomicReference<>(LoadingState.NOT_LOADED);

    /**
     * Internal, mutable list of {@link Series} which {@link #loadSeries(List)}
     * updates.
     */
    private final ArrayList<Series> series = new ArrayList<>(100);

    /**
     * Abstract method which subclasses implement with their series loading
     * logic.
     *
     * @param seriesList List of {@link Series} which the subclass updates as
     * it loads series.
     */
    protected abstract void loadSeries(List<Series> seriesList) throws Exception;

    @Override
    public List<Series> getSeries() {
        // If we're in the NOT_LOADED state we need to start loading manga
        if (state.get() == LoadingState.NOT_LOADED) {
            // In a new thread, load series
            MangaCrawler2.threadPool.submit(() -> {
                try {
                    state.set(LoadingState.LOADING);
                    loadSeries(series);
                    state.set(LoadingState.LOADED);
                } catch (Throwable t) {
                    t.printStackTrace();
                    state.set(LoadingState.LOAD_FAILED);
                }
            });

            // State may be set to LOADING before now, so we must only set
            // WAITING if we're in the NOT_LOADED state
            state.compareAndSet(LoadingState.NOT_LOADED, LoadingState.WAITING);
        }

        // Return an unmodifiable view of the list so callers cannot modify series
        return Collections.unmodifiableList(series);
    }

    @Override
    public LoadingState getState() {
        return state.get();
    }
}
