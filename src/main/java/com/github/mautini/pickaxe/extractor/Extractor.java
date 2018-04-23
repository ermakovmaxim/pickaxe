package com.github.mautini.pickaxe.extractor;

import com.github.mautini.pickaxe.model.Schema;
import org.jsoup.nodes.Document;

import java.util.List;

@FunctionalInterface
public interface Extractor {

    List<Schema> getThings(Document document);
}
