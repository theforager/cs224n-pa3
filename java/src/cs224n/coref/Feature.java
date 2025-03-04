package cs224n.coref;

import cs224n.util.Pair;

import java.util.Set;

/**
 * @author Gabor Angeli (angeli at cs.stanford)
 */
public interface Feature {

  //-----------------------------------------------------------
  // TEMPLATE FEATURE TEMPLATES
  //-----------------------------------------------------------
  public static class PairFeature implements Feature {
    public final Pair<Feature,Feature> content;
    public PairFeature(Feature a, Feature b){ this.content = Pair.make(a, b); }
    public String toString(){ return content.toString(); }
    public boolean equals(Object o){ return o instanceof PairFeature && ((PairFeature) o).content.equals(content); }
    public int hashCode(){ return content.hashCode(); }
  }

  public static abstract class Indicator implements Feature {
    public final boolean value;
    public Indicator(boolean value){ this.value = value; }
    public boolean equals(Object o){ return o instanceof Indicator && o.getClass().equals(this.getClass()) && ((Indicator) o).value == value; }
    public int hashCode(){ 
    	return this.getClass().hashCode() ^ Boolean.valueOf(value).hashCode(); }
    public String toString(){ 
    	return this.getClass().getSimpleName() + "(" + value + ")"; }
  }

  public static abstract class IntIndicator implements Feature {
    public final int value;
    public IntIndicator(int value){ this.value = value; }
    public boolean equals(Object o){ return o instanceof IntIndicator && o.getClass().equals(this.getClass()) && ((IntIndicator) o).value == value; }
    public int hashCode(){ 
    	return this.getClass().hashCode() ^ value; 
    }
    public String toString(){ return this.getClass().getSimpleName() + "(" + value + ")"; }
  }

  public static abstract class BucketIndicator implements Feature {
    public final int bucket;
    public final int numBuckets;
    public BucketIndicator(int value, int max, int numBuckets){
      this.numBuckets = numBuckets;
      bucket = value * numBuckets / max;
      if(bucket < 0 || bucket >= numBuckets){ throw new IllegalStateException("Bucket out of range: " + value + " max="+max+" numbuckets="+numBuckets); }
    }
    public boolean equals(Object o){ return o instanceof BucketIndicator && o.getClass().equals(this.getClass()) && ((BucketIndicator) o).bucket == bucket; }
    public int hashCode(){ return this.getClass().hashCode() ^ bucket; }
    public String toString(){ return this.getClass().getSimpleName() + "(" + bucket + "/" + numBuckets + ")"; }
  }

  public static abstract class Placeholder implements Feature {
    public Placeholder(){ }
    public boolean equals(Object o){ return o instanceof Placeholder && o.getClass().equals(this.getClass()); }
    public int hashCode(){ return this.getClass().hashCode(); }
    public String toString(){ return this.getClass().getSimpleName(); }
  }

  public static abstract class StringIndicator implements Feature {
    public final String str;
    public StringIndicator(String str){ this.str = str; }
    public boolean equals(Object o){ return o instanceof StringIndicator && o.getClass().equals(this.getClass()) && ((StringIndicator) o).str.equals(this.str); }
    public int hashCode(){ return this.getClass().hashCode() ^ str.hashCode(); }
    public String toString(){ return this.getClass().getSimpleName() + "(" + str + ")"; }
  }

  public static abstract class SetIndicator implements Feature {
    public final Set<String> set;
    public SetIndicator(Set<String> set){ this.set = set; }
    public boolean equals(Object o){ return o instanceof SetIndicator && o.getClass().equals(this.getClass()) && ((SetIndicator) o).set.equals(this.set); }
    public int hashCode(){ return this.getClass().hashCode() ^ set.hashCode(); }
    public String toString(){
      StringBuilder b = new StringBuilder();
      b.append(this.getClass().getSimpleName());
      b.append("( ");
      for(String s : set){
        b.append(s).append(" ");
      }
      b.append(")");
      return b.toString();
    }
  }
  
  /*
   * TODO: If necessary, add new feature types
   */

  //-----------------------------------------------------------
  // REAL FEATURE TEMPLATES
  //-----------------------------------------------------------

  public static class CoreferentIndicator extends Indicator {
    public CoreferentIndicator(boolean coreferent){ super(coreferent); }
  }

  public static class ExactMatch extends Indicator {
    public ExactMatch(boolean exactMatch){ super(exactMatch); }
  }
  
  public static class HW_Exact extends Indicator {
    public HW_Exact(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_Gender extends Indicator {
    public HW_Gender(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_PoS extends Indicator {
    public HW_PoS(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_NER extends Indicator {
    public HW_NER(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_Lemma extends Indicator {
    public HW_Lemma(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_Noun extends Indicator {
    public HW_Noun(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_ProperNoun extends Indicator {
    public HW_ProperNoun(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_PluralNoun extends Indicator {
    public HW_PluralNoun(boolean exactMatch){ super(exactMatch); }
  }
  
  
  /*
   * TODO: Add values to the indicators here.
   */
  public static class HW_WordInclusion extends Indicator {
    public HW_WordInclusion(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_CompatibleModifiers extends Indicator {
    public HW_CompatibleModifiers(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_Unigram extends Indicator {
    public HW_Unigram(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_Bigram extends Indicator {
    public HW_Bigram(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_OnePronoun extends Indicator {
    public HW_OnePronoun(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_BothPronoun extends Indicator {
    public HW_BothPronoun(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_BothContainUppercase extends Indicator {
    public HW_BothContainUppercase(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_NumberMatch extends Indicator {
    public HW_NumberMatch(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_StrictNumberMatch extends Indicator {
    public HW_StrictNumberMatch(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_GenderMatch extends Indicator {
    public HW_GenderMatch(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_StrictGenderMatch extends Indicator {
    public HW_StrictGenderMatch(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_PersonMatch extends Indicator {
    public HW_PersonMatch(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_StrictPersonMatch extends Indicator {
    public HW_StrictPersonMatch(boolean exactMatch){ super(exactMatch); }
  }
  public static class HW_OverlapCount extends IntIndicator {
    public HW_OverlapCount(int exactMatch){ super(exactMatch); }
  }
  public static class HW_MentionDist extends IntIndicator {
    public HW_MentionDist(int exactMatch){ super(exactMatch); }
  }
  public static class HW_SentenceDist extends IntIndicator {
    public HW_SentenceDist(int exactMatch){ super(exactMatch); }
  }

}
