import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleProcessor {

    private Map<String, JournalJ> mapJournals = new HashMap<>();

    void parseJournals(List<JournalJ> journals) {

        mapJournals.clear();
        journals.stream().forEach(j -> mapJournals.put(j.id, j));
    }

    void parseKeywords(List<String> keywords) {

    }

    void parseArticle(ArticleJ article) {

        article.journal = mapJournals.get(article.journalId);

        // Doing something...
        System.out.println(String.format("%s - %s [%d]", article.id, article.title, article.body.length()));
    }
}
