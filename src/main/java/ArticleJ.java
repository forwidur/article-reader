import java.util.List;

public class ArticleJ {

    public final String id;
    public final String title;
    public final String journalId;
    public JournalJ journal;
    public final List<String> keywords;
    public final List<String> topics;
    public final List<String> types;
    public final String body;

    public ArticleJ(String id, String title, String journalId, List<String> keywords, List<String> topics, List<String> types, String body) {
        this.id = id;
        this.title = title;
        this.journalId = journalId;
        this.keywords = keywords;
        this.topics = topics;
        this.types = types;
        this.body = body;
    }
}
