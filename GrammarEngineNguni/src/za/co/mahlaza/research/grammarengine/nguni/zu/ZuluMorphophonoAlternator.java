package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.MorphophonoAlternator;
import za.co.mahlaza.research.grammarengine.nguni.NguniVowelDetector;
import za.co.mahlaza.research.grammarengine.nguni.VowelPosition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ZuluMorphophonoAlternator  implements MorphophonoAlternator {

    NguniVowelDetector vowelDetector;

    public ZuluMorphophonoAlternator() {
        vowelDetector = new NguniVowelDetector();
    }

    @Override
    public String joinMorpheme(String leftMorpheme, String rightMorpheme) throws Exception {
        if (leftMorpheme == null || rightMorpheme == null) {
            throw new NullPointerException("The left or rightmorpheme are currently null. You cannot append null.");
        }

        String result = "";
        if (isConditioningNeeded(leftMorpheme, rightMorpheme)) {
            char leftChar = leftMorpheme.charAt(leftMorpheme.length()-1);
            char rightChar = rightMorpheme.charAt(0);
            Queue<String> vals = mutate(leftChar, rightChar, leftMorpheme, rightMorpheme);
            for (String val : vals) {
                result +=  val;
            }
        } else {
            result = leftMorpheme + rightMorpheme;
        }
        return result;
    }

    public boolean isConditioningNeeded(String left, String right) {
        if (left.isEmpty() || right.isEmpty()) {
            return false;
        }

        char leftEnd = left.charAt(left.length()-1);
        char rightStart = right.charAt(0);
        boolean vowelsFollowEachOTher = vowelDetector.isVowel(leftEnd) && vowelDetector.isVowel(rightStart);

        if (vowelsFollowEachOTher) {
            return true;
        }
        else {
            if (left.length() > 1 && left.matches(".*n[aeiou]")) {
                String[] leadRightMorphemes = {"b", "f", "s", "v", "z", "dl", "hl", "kh", "sh", "th"};
                for (String leadMorpheme : leadRightMorphemes) {
                    if (right.startsWith(leadMorpheme)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Queue<String> mutate(char leftChar, char rightChar, String leftMorpheme, String rightMorpheme) {

        Queue<String> result = new LinkedList<>();

        char secondLastLeftChar = '\0';
        String twoLeftCharsBeforeLastChar = "";
        if (leftMorpheme.length()>=2) {
            secondLastLeftChar = leftMorpheme.charAt(leftMorpheme.length() - 2);
        }
        if (leftMorpheme.length()>=3) {
            twoLeftCharsBeforeLastChar = leftMorpheme.substring(leftMorpheme.length() - 3, leftMorpheme.length()-1);
        }

        String newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 1);
        String newRightMorpheme = rightMorpheme.substring(1);

        //Taken from two sources:
        //P.E Raper, The Zulu Language, Acta Academia
        //S. Naidoo, Intrusive stop formation in Zulu: an application of feature geometry theory, Stellenbosch Uni., 2005
        String[] leadRightMorphemes = {"b", "f", "s", "v", "z", "dl", "hl", "kh", "sh", "th"};
        if (leftMorpheme.length() > 1 && leftMorpheme.matches(".*n[aeiou]") && Stream.of(leadRightMorphemes).anyMatch(lead -> rightMorpheme.startsWith(lead))) {
            if (rightMorpheme.startsWith("b")) {
                result.add(leftMorpheme);
                result.add("mb");
                result.add(rightMorpheme.substring(1));
            }
            else if (rightMorpheme.startsWith("f")) {
                result.add(leftMorpheme);
                result.add("mf");
                result.add(rightMorpheme.substring(1));
            }
            else if (rightMorpheme.startsWith("s")) {
                result.add(leftMorpheme);
                result.add("ns");
                result.add(rightMorpheme.substring(1));
            }
            else if (rightMorpheme.startsWith("v")) {
                result.add(leftMorpheme);
                result.add("mv");
                result.add(rightMorpheme.substring(1));
            }
            else if (rightMorpheme.startsWith("z")) {
                result.add(leftMorpheme);
                result.add("nz");
                result.add(rightMorpheme.substring(1));
            }
            else if (rightMorpheme.startsWith("dl")) {
                result.add(leftMorpheme);
                result.add("ndl");
                result.add(rightMorpheme.substring(2));
            }
            else if (rightMorpheme.startsWith("hl")) {
                result.add(leftMorpheme);
                result.add("nhl");
                result.add(rightMorpheme.substring(2));
            }
            else if (rightMorpheme.startsWith("kh")) {
                result.add(leftMorpheme);
                result.add("nk");
                result.add(rightMorpheme.substring(2));
            }
            else if (rightMorpheme.startsWith("sh")) {
                result.add(leftMorpheme);
                result.add("ntsh");
                result.add(rightMorpheme.substring(2));
            }
            else if (rightMorpheme.startsWith("th")) {
                result.add(leftMorpheme);
                result.add("nt");
                result.add(rightMorpheme.substring(2));
            }
        }
        //Galen Sibanda's basic gliding
        else if (leftChar=='i' && rightChar=='e') {
            result.add(newLeftMorpheme);
            //Gliding with Consonant
            if (secondLastLeftChar!='\0' &&!vowelDetector.isVowel(secondLastLeftChar)) {
                result.add("e");
            } else {
                result.add("ye");
            }
            result.add(newRightMorpheme);
        }
        else if (leftChar=='i' && rightChar=='a') {
            result.add(newLeftMorpheme);
            //Gliding with Consonant
            if (secondLastLeftChar!='\0' &&!vowelDetector.isVowel(secondLastLeftChar)) {
                result.add("a");
            } else {
                result.add("ya");
            }
            result.add(newRightMorpheme);
        }
        else if (leftChar=='i' && rightChar=='o') {
            result.add(newLeftMorpheme);
            //Gliding with Consonant
            if (secondLastLeftChar!='\0' && !vowelDetector.isVowel(secondLastLeftChar)) {
                result.add("o");
            } else {
                result.add("yo");
            }
            result.add(newRightMorpheme);
        }
        //this if stmt is relying on the appendix found in Andrew van der Spuy's "Bilabial Palatalisation in Zulu"
        else if (leftChar == 'u' && vowelDetector.isVowel(rightChar)) {
            if ((!twoLeftCharsBeforeLastChar.isEmpty() || secondLastLeftChar!='\0') && (vowelDetector.isLabial(secondLastLeftChar) || vowelDetector.isLabial(twoLeftCharsBeforeLastChar))) {
                    result.add(newLeftMorpheme);
                    result.add("");
                    result.add(rightMorpheme);
            }
            else if (secondLastLeftChar!='\0' && !vowelDetector.isVowel(secondLastLeftChar)) {
                if (!vowelDetector.isLabial(secondLastLeftChar) && vowelDetector.isVowelPosition(rightChar, VowelPosition.BACK)) {
                    result.add(newLeftMorpheme);
                    result.add("");
                    result.add(rightMorpheme);
                }
                else if (!vowelDetector.isLabial(secondLastLeftChar)) {
                    result.add(newLeftMorpheme);
                    result.add("w");
                    result.add(rightMorpheme);
                }
            }
            else if (leftChar == 'u') {
                result.add("");
                result.add("w");
                result.add(rightMorpheme);
            }
        }
        //Galen Sibanda's Vowel deletion
        else if (leftChar=='a' && rightChar=='e') {
            result.add(newLeftMorpheme);
            result.add("e");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='a' && rightChar=='o') {
            result.add(newLeftMorpheme);
            result.add("o");
            result.add(newRightMorpheme);
        }
        //Lionel Posthumus's "The indicative negative morpheme (k)a- followed by a subject morpheme that comprises a vowel only"
        else if ((leftMorpheme.equals("ka") || leftMorpheme.equals("a")) && (rightChar == 'u' || rightChar == 'i' || rightChar == 'a')) {
            result.add(leftMorpheme);
            if (rightChar == 'i') {
                result.add("y");
            } else {
                result.add("w");
            }
            result.add(rightMorpheme);
        }
        //Galen Sibanda's Coalesence
        else if (leftChar=='a' && rightChar=='a') {
            result.add(newLeftMorpheme);
            result.add("a");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='a' && rightChar=='i') {
            result.add(newLeftMorpheme);
            result.add("e");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='a' && rightChar=='u') {
            result.add(newLeftMorpheme);
            result.add("o");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='i' && rightChar=='i') {
            result.add(newLeftMorpheme);
            result.add("i");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='e' && rightChar=='e') {
            result.add(newLeftMorpheme);
            result.add("e");
            result.add(newRightMorpheme);
        }
        //The following rule is taken from Galen Sibanda
        else if (leftChar=='e' && rightChar=='a') {
            result.add(newLeftMorpheme);
            result.add("ya");
            result.add(newRightMorpheme);
        }
        //I am relying on ZulMorph for the following if stmt
        else if (leftChar=='e' && rightChar=='u') {
            result.add(newLeftMorpheme);
            result.add("e");
            result.add(newRightMorpheme);
        }
        //I am relying on ZulMorph for the following if stmt
        else if (leftChar=='e' && rightChar=='i') {
            result.add(newLeftMorpheme);
            result.add("e");
            result.add(newRightMorpheme);
        }
        //Lionel Posthumus' "The combination of a front vowel phoneme followed by a back vowel phoneme"
        else if (leftChar=='i' && rightChar=='u') {
            result.add(newLeftMorpheme);
            result.add("u");
            result.add(newRightMorpheme);
        }
        //Lionel Posthumus' "The combination of a back vowel phoneme followed by a front vowel phoneme"
        else if (leftChar=='o' && rightChar=='i') {
            result.add(newLeftMorpheme);
            result.add("we");
            result.add(newRightMorpheme);
        }
        //Lionel Posthumus' "The combination of a back vowel phoneme followed by a front vowel phoneme"
        else if (leftChar=='o' && rightChar=='u') {
            result.add(newLeftMorpheme);
            result.add("u");
            result.add(newRightMorpheme);
        }
        //Lionel Posthumus' "The combination of a back vowel phoneme followed by a front vowel phoneme"
        else if (leftChar=='u' && rightChar=='o') {
            result.add(newLeftMorpheme);
            result.add("o"); //According to Lionel Posthumus, its supposed to be /wo/ however the /w/ is elided
            result.add(newRightMorpheme);
        }
        //Lionel Posthumus' "The combination of a back vowel phoneme followed by a front vowel phoneme"
        else if (leftChar=='i' && rightChar=='o') {
            if (leftMorpheme.length() > 1) {
                result.add(newLeftMorpheme);
                result.add("o");
                result.add(newRightMorpheme);
            }
            else {
                result.add(newLeftMorpheme);
                result.add("yo");
                result.add(newRightMorpheme);
            }
        }
        //Lionel Posthumus' "The combination of a back vowel phoneme followed by a front vowel phoneme"
        else if (leftChar=='e' && rightChar=='o') {
            result.add(newLeftMorpheme);
            result.add("o");
            result.add(newRightMorpheme);
        }
        //The following two are Galen Sibanda's rules for 'o'
        else if (leftChar=='o' && rightChar=='a') {
            result.add(newLeftMorpheme);
            result.add("wa");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='o' && rightChar=='e') {
            result.add(newLeftMorpheme);
            result.add("we");
            result.add(newRightMorpheme);
        }
        //M.H. Mpungose's Analysis of the Word-Initial Segment with Reference to Lemmatising Zulu Nasal Nouns
        if (leftChar == 'n' && rightChar == 'n') {
            result.add(newLeftMorpheme);
            result.add("n");
            result.add(newRightMorpheme);
        }
        //M.H. Mpungose's Analysis of the Word-Initial Segment with Reference to Lemmatising Zulu Nasal Nouns
        else if (leftChar == 'n' && rightChar == 'm') {
            result.add(newLeftMorpheme);
            result.add("m");
            result.add(newRightMorpheme);
        }

        //TODO: check if you need to palatise
        return result;
    }

    //Andrew van der Spuy's "Bilabial Palatalisation in Zulu" and Shamila Naidoo's "The palatalisation process in isiZulu revisited"
    private String palatilise (String leftChars) {
        switch(leftChars) {
            //bilabials
            case "ph": {
                return "sh";
            }
            case "bh": {
                return "j";
            }
            case "b": {
                return "tsh";
            }
            case "p": {
                return "tsh";
            }
            case "m" : {
                return "ny";
            }
            case "mb": {
                return "nj";
            }
            case "mp": {
                return "ntsh";
            }
            //alveolars
            case "d": {
                return "j";
            }
            case "nd": {
                return "nj";
            }
            case "n": {
                return "ny";
            }
            case "th": {
                return "sh";
            }
            case "nt": {
                return "ntsh";
            }
            case "t": {
                return "tsh";
            }
        }
        return null;
    }

}
