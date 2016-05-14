package io.sushisquad.mangacrawler2.lib.provider.mangaeden;

import io.sushisquad.mangacrawler2.lib.Chapter;
import io.sushisquad.mangacrawler2.lib.MangaProvider;
import io.sushisquad.mangacrawler2.lib.SeriesBase;

import java.net.URI;
import java.util.List;

public class MangaEdenSeries extends SeriesBase {
    private final MangaProvider provider;

    protected String id;
    protected URI imageURI;
    protected String title;

    public MangaEdenSeries(MangaProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void loadChapters(List<Chapter> chapterList) throws Exception {

    }

    @Override
    public URI getImageURI() {
        return imageURI;
    }

    @Override
    public MangaProvider getProvider() {
        return provider;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
