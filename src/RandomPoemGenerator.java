import java.io.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.lang.RuntimeException;

public class RandomPoemGenerator {

    static Map<String, Map<String, String>> gramRules = new HashMap<>();
    static Map<String, String> intermediateRule = new HashMap<>();

    static String poemRule = "";
    static String lineRule = "";

    static Random rand = new Random();


    public static void main(String[] args) throws Exception {

    // read the text file
    File file = new File("./poem_generator_rules.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));

    String line;
    while ((line = br.readLine()) != null) {

        String [] lineContent = line.toLowerCase().split(": ");
        if (lineContent.length <= 1)  {
            throw new RuntimeException("Bad rule, expected rule format is 'ruleName: content content...'");
        }

        String ruleName = lineContent[0];

        if (ruleName.equals("poem")) {
            poemRule = lineContent[1];
        }
        else if (ruleName.equals("line")) {
            lineRule = lineContent[1];
        }
        else {  // rules for adjectives, verbs, nouns, pronouns and prepositions all follow the same format, parse them a map of maps for easier lookup
            String[] ruleContent = lineContent[1].split(" ");

            if (ruleContent.length != 2) {
                throw new RuntimeException("Bad rule, check if grammatical rules are correctly defined, expected format 'ruleName: wordlist referencelist'");
            }

            else {
                String words = ruleContent[0];     // get words
                String ref = ruleContent[1];      // get references

                String wordKey = ruleName.concat("_words");
                String refKey = ruleName.concat("_references");

                intermediateRule.put(wordKey, words);
                intermediateRule.put(refKey, ref);

                gramRules.put(lineContent[0], intermediateRule);
            }
        }
    }
    poem();
    }

    static String word(String words) {
        String[] wordsList = words.split("\\|");
        int s = rand.nextInt(wordsList.length);     //get random word from the list
        return wordsList[s] + " ";
    }

    static void reference (String rule) {

        String ruleLookup;
        String[] references = rule.split("\\|");

        if (references.length > 0) {
            int s = rand.nextInt(references.length);    //get random rule reference from the list
            ruleLookup = references[s];
        } else {
            ruleLookup = rule;
        }

        if (ruleLookup.equals("<adjective>"))
            adjective();
        else if (ruleLookup.equals("<noun>"))
            noun();
        else if (ruleLookup.equals("<pronoun>"))
            pronoun();
        else if (ruleLookup.equals("<verb>"))
            verb();
        else if (ruleLookup.equals("<preposition>"))
            preposition();
        else if (ruleLookup.equals("$linebreak"))
            System.out.print("\r");
        else
            System.out.print("\n");
    }

    static void poem() {
        String[] poemRuleContent = poemRule.split(" ");

        int i = 0;
        while (i < poemRuleContent.length) {
            line();
            i++;
        }
    }

    static void line() {
        String[] lineRuleContent = lineRule.split(" ");

        int i = 0;
        while (i < lineRuleContent.length) {
            reference(lineRuleContent[i]);
            i++;
        }
    }

    static void adjective() {
        System.out.print(word(gramRules.get("adjective").get("adjective_words")));
        reference(gramRules.get("adjective").get("adjective_references"));
    }

    static void noun() {
        System.out.print(word(gramRules.get("noun").get("noun_words")));
        reference(gramRules.get("noun").get("noun_references"));
    }

    static void pronoun() {
        System.out.print(word(gramRules.get("pronoun").get("pronoun_words")));
        reference(gramRules.get("pronoun").get("pronoun_references"));
    }
    static void verb() {
        System.out.print(word(gramRules.get("verb").get("verb_words")));
        reference(gramRules.get("verb").get("verb_references"));
    }
    static void preposition() {
        System.out.print(word(gramRules.get("preposition").get("preposition_words")));
        reference(gramRules.get("preposition").get("preposition_references"));
    }

}