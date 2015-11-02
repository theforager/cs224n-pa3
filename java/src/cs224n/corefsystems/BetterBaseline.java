package cs224n.corefsystems;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import cs224n.coref.*;
import cs224n.util.CounterMap;
import cs224n.util.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class BetterBaseline implements CoreferenceSystem {
  
  public CounterMap<String,String> synonyms = new CounterMap<String,String>();
  
  // Create word sets that we will treat differently and/or exclude from certain rules
  public static final String[] ALL_PRONOUNS = new String[] {"i","you","he","she","it","me","us","we","them",
      "him","her","his","hers","my","yours","ours","our"};
  public static final String[] ALL_ARTICLES = new String[] {"a","an","the"};
  public static final String[] ALL_PARSE_NOUNS = new String[] {"NN","NNS","NNP","NNPS"};
  
  public static final Set<String> pronouns = new HashSet<String>(Arrays.asList(ALL_PRONOUNS));
  public static final Set<String> articles = new HashSet<String>(Arrays.asList(ALL_ARTICLES));
  public static final Set<String> parseNouns = new HashSet<String>(Arrays.asList(ALL_PARSE_NOUNS));


  @Override
  public void train(Collection<Pair<Document, List<Entity>>> trainingData) {
    /*for(Pair<Document, List<Entity>> pair : trainingData){
      List<Entity> clusters = pair.getSecond();
      
      trainSynonyms(clusters);
    }*/
  }

  @Override
  public List<ClusteredMention> runCoreference(Document doc) {
    List<ClusteredMention> output;
    
    // Phase 1: Exact Matches
    output = exactMatch(null, doc);
    
    // Phase 2: Partial Matches
    output = partialMatch(output, doc);
    
    return output;
  }
  
  
  /**
   * Cluster all mentions that are exact string matches
   */
  public List<ClusteredMention> exactMatch(List<ClusteredMention> currentClusters, Document doc) {
    Map<String,Entity> clusters = new HashMap<String,Entity>();
    List<ClusteredMention> output = new ArrayList<ClusteredMention>();
    
    for (Mention m : doc.getMentions()) {
      String mentionString = m.gloss().toLowerCase();
      if (clusters.containsKey(mentionString)) {
        output.add(m.markCoreferent(clusters.get(mentionString)));
      } else {
        ClusteredMention newCluster = m.markSingleton();
        output.add(newCluster);
      }
    }
    return output;
  }
  
  /**
   * Combine clusters with some percent overlap in 
   */
  public List<ClusteredMention> partialMatch(List<ClusteredMention> currentClusters, Document doc) {
    List<ClusteredMention> output = new ArrayList<ClusteredMention>();
    
    for (ClusteredMention curr : currentClusters) {
      String text = curr.mention.gloss();
      Entity bestMatch = getBestMatch(currentClusters, text);
      
      if (bestMatch != null) {
        curr.mention.changeCoreference(bestMatch);
      }
      output.add(curr);
    }
    return output;
  }
  

  /**
   * Use the overlap percent to decide if a String Set contains a specific string
   */
  private static final double MATCH_THRESHOLD = 0.67;
  
  public Entity getBestMatch(List<ClusteredMention> clusters, String newText) {
    Entity bestEntity = null;
    double bestOverlap = 0.0;
    
    if (clusters != null) {
      for (ClusteredMention cm : clusters) {
        String clusterText = cm.mention.gloss();
        double overlap = countOverlapPercent(clusterText, newText);
        if (overlap > MATCH_THRESHOLD && overlap > bestOverlap) {
          bestOverlap = overlap;
          bestEntity = cm.entity;
        }
      }
    }
    return bestEntity;
  }
  
  
  /**
   * Compute the percent of the words the two string overlap with each other
   */
  public double countOverlapPercent (String a, String b) {
    int nWord  = 0;
    int nCommon = 0;
    String longStr;
    String shortStr;
    if (a.length() > b.length()){
      longStr = a;
      shortStr =b;
    }
    else{
      longStr = b;
      shortStr =a;
    }

    for (String word : longStr.split(" ")){
      if (word!="the"){
        nWord++;
        if (shortStr.toLowerCase().contains(word.toLowerCase()))
          nCommon++;
        }    
    }
    double commonRatio =nCommon* 1.0/nWord;
    return commonRatio;
  }
  
  /**
   * Updates CounterMap of co-occurences in the mention words
   */
  public void trainSynonyms(List<Entity> clusters) {
    for (Entity e : clusters) {
      // Put all mention words into a List
      List<String> mentionWords = new ArrayList<String>();
      
      for (Mention mention : e.mentions) {
        for (String word : mention.gloss().split(" ")) {
          if (!isPronoun(word) && !isArticle(word)) {
            mentionWords.add(word);
          }
        }
      }
      
      // Store count of all co-occurences 
      for (int i=0; i < mentionWords.size(); i++) {
        String iWord = mentionWords.get(i);
        for (int j=0; j < mentionWords.size(); j++) {
          if (i != j) {
            String jWord = mentionWords.get(j);
            synonyms.incrementCount(iWord, jWord, 1);
          }
        }
      }
    }
  }
  
  
  /** 
   * check if two string contain exactly the same set of words which may not be in the same order
   */
  public boolean containsUnorderredWords(String a, String b) {
    boolean flag = true;
    for (String word : a.split(" ")){
      if (!b.toLowerCase().contains(word.toLowerCase()))
        flag = false;
    }    
    return flag;
  }
  
  
  /**
   * Returns TRUE if the word is a pronoun, otherwise FALSE
   */
  public boolean parseIsNoun(String tag) {
    if (parseNouns.contains(tag.toLowerCase())) {
      return true;
    } else {
      return false;
    }
  }
  
  
  /**
   * Returns TRUE if the word is a pronoun, otherwise FALSE
   */
  public boolean isPronoun(String word) {
    if (pronouns.contains(word.toLowerCase())) {
      return true;
    } else {
      return false;
    }
  }
  
  
  /**
   * Returns TRUE if the word is an article, otherwise FALSE
   */
  public boolean isArticle(String word) {
    if (articles.contains(word.toLowerCase())) {
      return true;
    } else {
      return false;
    }
  }
  
  
  /**
   * Get all entities present in the ClusteredMention list
   */
  public Set<Entity> getEntities(List<ClusteredMention> clusters) {
    Set<Entity> entities = new HashSet<Entity>();
    for (ClusteredMention cm: clusters) {
      entities.add(cm.entity);
    }
    return entities;
  }
}