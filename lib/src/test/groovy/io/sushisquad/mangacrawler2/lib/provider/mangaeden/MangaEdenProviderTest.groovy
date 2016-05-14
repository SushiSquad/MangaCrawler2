package io.sushisquad.mangacrawler2.lib.provider.mangaeden

import io.sushisquad.mangacrawler2.lib.LoadingState
import io.sushisquad.mangacrawler2.lib.MangaProvider
import spock.lang.Specification

class MangaEdenProviderTest extends Specification {
    def "successfully loads series"() {
        setup:
        MangaProvider provider = new MangaEdenProvider()

        when:
        provider.getSeries()

        while (provider.state != LoadingState.LOADED &&
               provider.state != LoadingState.LOAD_FAILED) {
            sleep(100)
        }

        then:
        provider.state == LoadingState.LOADED
        provider.series.size() >= 16982 // Number of manga on 2016-05-14
    }
}
