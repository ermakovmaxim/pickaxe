package com.github.mautini.pickaxe;

import com.github.mautini.pickaxe.converter.SchemaToThingConverter;
import com.github.mautini.pickaxe.extractor.MicrodataExtractor;
import com.google.schemaorg.JsonLdSerializer;
import com.google.schemaorg.JsonLdSyntaxException;
import com.google.schemaorg.core.Thing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private JsonLdSerializer jsonLdSerializer;

    public Scraper() {
        jsonLdSerializer = new JsonLdSerializer(true);
    }

    public List<Thing> thing(File file) throws IOException {
        Document document = Jsoup.parse(file, "UTF-8");
        return thing(document);
    }

    public List<Thing> thing(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return thing(document);
    }

    private List<Thing> thing(Document document) {
        return new MicrodataExtractor().getThings(document).stream()
                .map(SchemaToThingConverter::convert)
                .collect(Collectors.toList());
    }

    public List<String> json(File file) throws IOException {
        Document document = Jsoup.parse(file, "UTF-8");
        return json(document);
    }

    public List<String> json(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return json(document);
    }

    private List<String> json(Document document) {
        return new MicrodataExtractor().getThings(document).stream()
                .map(SchemaToThingConverter::convert)
                .map(thing -> parseThings(thing))
                .collect(Collectors.toList());
    }

    private String parseThings(Thing thing) {
        try {
            return jsonLdSerializer.serialize(thing);
        } catch (JsonLdSyntaxException e) {
            LOGGER.warn("Error during the microdata parsing", e);
            return null;
        }
    }
}
