import java.util.List;

public class JournalJ {

    public final String id;
    public final String title;
    public final String publisherName;
    public final List<String> topics;

    public JournalJ(String id, String title, String publisherName, List<String> topics) {

        this.id = id;
        this.title = title;
        this.publisherName = publisherName;
        this.topics = topics;
    }
}
