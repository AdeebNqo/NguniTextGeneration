package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.MorphophonoAlternator;
import za.co.mahlaza.research.grammarengine.nguni.NguniVowelDetector;
import za.co.mahlaza.research.grammarengine.nguni.VowelPosition;

import java.util.LinkedList;
import java.util.Queue;

public class ZuluMorphophonoAlternator  implements MorphophonoAlternator {

    NguniVowelDetector vowelDetector;

    public ZuluMorphophonoAlternator() {
        vowelDetector = new NguniVowelDetector();
    }

    @Override
    public String joinMorpheme(String leftMorpheme, String rightMorpheme) {
        String result;
        if (isConditioningNeeded(leftMorpheme, rightMorpheme)) {
            char leftChar = leftMorpheme.charAt(leftMorpheme.length()-1);
            char rightChar = rightMorpheme.charAt(0);
            Queue<String> vals = mutate(leftChar, rightChar, leftMorpheme, rightMorpheme);
            result =  vals.poll() + vals.poll() + vals.poll();
        } else {
            result = leftMorpheme + rightMorpheme;
        }
        return result;
    }

    public boolean isConditioningNeeded(String left, String right) {
        char leftEnd = left.charAt(left.length()-1);
        char rightStart = right.charAt(0);
        return vowelDetector.isVowel(leftEnd) && vowelDetector.isVowel(rightStart);
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

        //Galen Sibanda's basic gliding
        if (leftChar=='i' && rightChar=='e') {
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

        //The following two are Galen Sibanda's rules for 'o'
        else if (leftChar=='o' && rightChar=='a') {
            result.add(newLeftMorpheme);
            result.add("wa");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='0' && rightChar=='e') {
            result.add(newLeftMorpheme);
            result.add("we");
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
