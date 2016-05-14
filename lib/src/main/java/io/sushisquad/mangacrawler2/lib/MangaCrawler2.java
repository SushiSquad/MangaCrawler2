package io.sushisquad.mangacrawler2.lib;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class for holding constant values for the library.
 */
public class MangaCrawler2 {
    /**
     * A thread pool used for executing tasks created by MangaCrawler2.
     */
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    /**
     * Shared HttpClient for executing requests.
     */
    public static final HttpClient httpClient;
    static {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(10000)
            .setSocketTimeout(10000)
            .setConnectionRequestTimeout(10000)
            .build();

        httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .build();
    }
}
