import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.handler.EmitHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ArticleProcessor {

  private Map<String, JournalJ> journals_ = new HashMap<>();
  private final Map<String, Integer> keywords_ = new HashMap<>();
  private Trie trie_;
  private int cnt_ = 0;
  private long start_ = System.currentTimeMillis();

  void parseJournals(List<JournalJ> journals) {
    journals_.clear();
    journals.stream().forEach(j -> journals_.put(j.id, j));
  }

  void parseKeywords(List<String> keywords) {
    Set<String> exclude = getExcludedWords();

    System.err.println("Constructing trie...");
    long start = System.currentTimeMillis();
    Trie.TrieBuilder b = Trie.builder().removeOverlaps().onlyWholeWords();
    int cnt = 0;
    for (String k: keywords) {
      String c = clean(k);
      if (c == null || exclude.contains(c)) continue;
      b.addKeyword(c);
      keywords_.put(c, cnt++);
    }
    trie_ = b.build();
    System.err.println(String.format("Done. Took %dms. Seen %d raw words. %d final keywords.",
        System.currentTimeMillis() - start, keywords.size(), keywords_.size()));
  }

  private Set<String> getExcludedWords() {
    HashSet<String> res = new HashSet<>();
    LineIterator it = null;
    try {
      it = FileUtils.lineIterator(new File("excluded_keywords.txt"), "UTF-8");
      while (it.hasNext()) {
        String l = it.nextLine();
        if (l.startsWith("#")) continue;
        res.add(l);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      it.close();
    }
    return res;
  }

  private String clean(String k) {
    if (k.length() < 3 || StringUtils.isNumeric(k)) return null;
    return k.toLowerCase();
  }

  void parseArticle(ArticleJ article) {
    article.journal = journals_.get(article.journalId);

    final List<Integer> kwVector = extractKeywords(article);
    if (cnt_++ % 100 == 0) {
      System.err.println(String.format(
          "DOC %d %.2fs", cnt_, (System.currentTimeMillis() - start_) / 1e3));
    }

    String kws = kwVector.parallelStream()
        .map(k -> String.format("%d 1", k))
        .collect(Collectors.joining(" "));
    System.out.println(String.format(
        "%d %s %s", kwVector.size(), kws, article.id));
  }

  private List<Integer> extractKeywords(ArticleJ a) {
    List<Integer> res = new CopyOnWriteArrayList<>();
    final String b = a.body.toLowerCase();
    Set<Integer> seen = ConcurrentHashMap.newKeySet();
    trie_.parseText(b).parallelStream().forEach(e -> {
      String k = e.getKeyword();
      Integer idx = keywords_.get(k);
      if (idx == null) {
        System.err.println(String.format("NULL: %s", k));
        return;
      }
      if (!seen.contains(idx)) {
//        System.err.println(k);
        seen.add(idx);
        res.add(idx);
        // TODO: remember and exclude most popular kw.
      }
    });
    return res;
  }
}
