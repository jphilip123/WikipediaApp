package com.example.practice;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WikipediaAPI {
    public static void main(String[] args) {
        String pageTitle = "Golden_Isles_of_Georgia";
        String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&exintro&format=json&titles=" + pageTitle;

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonObject pagesObject = jsonObject.getAsJsonObject("query").getAsJsonObject("pages");
            String pageId = pagesObject.keySet().iterator().next();
            String extract = pagesObject.getAsJsonObject(pageId).get("extract").getAsString();

            // Remove HTML tags using regular expression
            String textOnly = extract.replaceAll("\\<.*?\\>", "");

            // Limit the output to 100 words
            String limitedText = limitWords(textOnly, 100);

            System.out.println(limitedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String limitWords(String text, int limit) {
        String[] words = text.split("\\s+");
        if (words.length <= limit) {
            return text;
        } else {
            StringBuilder limitedText = new StringBuilder();
            for (int i = 0; i < limit; i++) {
                limitedText.append(words[i]).append(" ");
            }
            return limitedText.toString().trim() + "...";
    }
}}
