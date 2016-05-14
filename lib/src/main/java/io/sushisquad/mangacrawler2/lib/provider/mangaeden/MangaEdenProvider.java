package io.sushisquad.mangacrawler2.lib.provider.mangaeden;

import io.sushisquad.mangacrawler2.lib.MangaCrawler2;
import io.sushisquad.mangacrawler2.lib.MangaProviderBase;
import io.sushisquad.mangacrawler2.lib.Series;
import io.sushisquad.mangacrawler2.lib.util.JSONPullParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class MangaEdenProvider extends MangaProviderBase {
    @Override
    public String getName() {
        return "Manga Eden";
    }

    @Override
    protected void loadSeries(List<Series> seriesList) throws Exception {
        HttpResponse response = MangaCrawler2.httpClient.execute(new HttpGet("https://www.mangaeden.com/api/list/0/"));
        JSONPullParser parser = new JSONPullParser(response.getEntity().getContent());

        parser.onKey("manga", () -> {
            parser.readArray(() -> {
                MangaEdenSeries series = new MangaEdenSeries(this);

                parser.readObject(key -> {
                    switch (key) {
                        case "i":
                            parser.readStringOrNull().ifPresent(id -> series.id = id);
                            break;
                        case "t":
                            parser.readStringOrNull().ifPresent(title -> series.title = title);
                            break;
                        case "im":
                            parser.readStringOrNull().ifPresent(imageString -> {
                                try {
                                    series.imageURI = new URI("https://cdn.mangaeden.com/mangasimg/" + imageString);
                                } catch (URISyntaxException ignored) {
                                }
                            });
                            break;
                        default:
                            parser.skip();
                            break;
                    }
                });

                if (series.id != null &&
                    series.title != null) {

                    seriesList.add(series);
                } else {
                    boolean a = false;
                    while (a) {}
                }
            });
        });
    }
}
