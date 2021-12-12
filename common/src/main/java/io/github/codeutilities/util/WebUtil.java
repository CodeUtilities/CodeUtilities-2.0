package io.github.codeutilities.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WebUtil {

    public static CompletableFuture<String> getStringAsync(String url) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> completableFuture.complete(response.body()));

        return completableFuture;
    }

}
