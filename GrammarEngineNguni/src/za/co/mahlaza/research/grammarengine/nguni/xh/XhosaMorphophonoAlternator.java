package za.co.mahlaza.research.grammarengine.nguni.xh;

import za.co.mahlaza.research.grammarengine.base.interfaces.MorphophonoAlternator;
import za.co.mahlaza.research.grammarengine.nguni.NguniVowelDetector;

import java.util.LinkedList;
import java.util.Queue;

public class XhosaMorphophonoAlternator implements MorphophonoAlternator {

    private NguniVowelDetector vowelDetector;

    public XhosaMorphophonoAlternator() {
        this.vowelDetector = new NguniVowelDetector();
    }

    @Override
    public String joinMorpheme(String leftMorpheme, String rightMorpheme) {
        String result = null;
        if (isConditioningNeeded(leftMorpheme, rightMorpheme)) {
            char leftChar = leftMorpheme.charAt(leftMorpheme.length()-1);
            char rightChar = rightMorpheme.charAt(0);
            Queue<String> vals = mutate(leftChar, rightChar, leftMorpheme, rightMorpheme);
            String prefix = vals.poll();
            String conditionedRoot = vals.poll();
            String suffix = vals.poll();
            result = prefix + conditionedRoot + suffix;
        } else {
            result = leftMorpheme + rightMorpheme;
        }
        return result;
    }

    public boolean isConditioningNeeded(String left, String right) {
        if (right.isEmpty()) {
            return false;
        }

        char leftEnd = '\0';
        if (left.length()>=1) {
            leftEnd = left.charAt(left.length() - 1);
        } else if (left.length() ==1) {
            leftEnd = left.charAt(0);
        }
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

        //basic gliding
        if (leftChar=='i' && rightChar=='e') {
            result.add(newLeftMorpheme);
            //Gliding with Consonant
            if (secondLastLeftChar!='\0' && !vowelDetector.isVowel(secondLastLeftChar)) {
                result.add("e");
            } else {
                result.add("ye");
            }
            result.add(newRightMorpheme);
        }
        else if (leftChar=='i' && rightChar=='a') {
            result.add(newLeftMorpheme);
            //Gliding with Consonant
            if (secondLastLeftChar!='\0' && !vowelDetector.isVowel(secondLastLeftChar)) {
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
        else if (leftChar=='u' && rightChar=='e') {
            //Gliding with initial bilabial
            if (vowelDetector.isLabial(twoLeftCharsBeforeLastChar) || vowelDetector.isLabial(secondLastLeftChar)) {
                //Gliding with non-initial bilabials
                if (leftMorpheme.length() > 3) {
                    if (!twoLeftCharsBeforeLastChar.equals("")) {
                        //bilabial has two chars
                        String palatizedBilabial = palatilise(twoLeftCharsBeforeLastChar, 'w');
                        newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 2);
                        result.add(newLeftMorpheme );
                        result.add(palatizedBilabial + "e");
                        result.add(newRightMorpheme);
                    } else {
                        //bilabial has one char
                        String palatizedBilabial = palatilise(String.valueOf(secondLastLeftChar), 'w');
                        result.add(newLeftMorpheme);
                        result.add(palatizedBilabial + "e");
                        result.add(newRightMorpheme);
                    }
                } else {
                    result.add(newLeftMorpheme);
                    result.add("e");
                    result.add(newRightMorpheme);
                }
            } else {
                result.add(newLeftMorpheme);
                result.add("we");
                result.add(newRightMorpheme);
            }
        }
        else if (leftChar=='u' && rightChar=='a') {
            //Gliding with initial bilabial
            if (vowelDetector.isLabial(twoLeftCharsBeforeLastChar) || vowelDetector.isLabial(secondLastLeftChar)) {
                //Gliding with non-initial bilabials
                if (leftMorpheme.length() > 3) {
                    if (!twoLeftCharsBeforeLastChar.equals("")) {
                        //bilabial has two chars
                        String palatizedBilabial = palatilise(twoLeftCharsBeforeLastChar, 'w');
                        newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 2);

                        result.add(newLeftMorpheme);
                        result.add(palatizedBilabial + "a");
                        result.add(newRightMorpheme);

                    } else {
                        //bilabial has one char
                        String palatizedBilabial = palatilise(String.valueOf(secondLastLeftChar), 'w');
                        newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 1);

                        result.add(newLeftMorpheme);
                        result.add(palatizedBilabial + "a");
                        result.add(newRightMorpheme);
                    }
                } else {
                    result.add(newLeftMorpheme);
                    result.add("a");
                    result.add(newRightMorpheme);
                }
            } else {
                result.add(newLeftMorpheme);
                result.add("wa");
                result.add(newRightMorpheme);
            }
        }
        else if (leftChar=='u' && rightChar=='o') {
            //Gliding with initial bilabial
            if (vowelDetector.isLabial(twoLeftCharsBeforeLastChar) || vowelDetector.isLabial(secondLastLeftChar)) {
                //Gliding with non-initial bilabials
                if (leftMorpheme.length() > 3) {
                    if (!twoLeftCharsBeforeLastChar.equals("")) {
                        //bilabial has two chars
                        String palatizedBilabial = palatilise(twoLeftCharsBeforeLastChar, 'w');
                        newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 2);

                        result.add(newLeftMorpheme);
                        result.add(palatizedBilabial + "o");
                        result.add(newRightMorpheme);
                    } else {
                        //bilabial has one char
                        String palatizedBilabial = palatilise(String.valueOf(secondLastLeftChar), 'w');
                        newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 1);

                        result.add(newLeftMorpheme);
                        result.add(palatizedBilabial + "o");
                        result.add(newRightMorpheme);
                    }
                } else {
                    result.add(newLeftMorpheme);
                    result.add("o");
                    result.add(newRightMorpheme);
                }
            } else {
                result.add(newLeftMorpheme);
                result.add("wo");
                result.add(newRightMorpheme);
            }
        }
        else if (leftChar=='u' && rightChar=='i') {
            //Gliding with initial bilabial
            if (vowelDetector.isLabial(twoLeftCharsBeforeLastChar) || vowelDetector.isLabial(secondLastLeftChar)) {
                //Gliding with non-initial bilabials
                if (leftMorpheme.length() > 3) {
                    if (!twoLeftCharsBeforeLastChar.equals("")) {
                        //bilabial has two chars
                        String palatizedBilabial = palatilise(twoLeftCharsBeforeLastChar, 'w');
                        newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 2);

                        result.add(newLeftMorpheme);
                        result.add(palatizedBilabial + "i");
                        result.add(newRightMorpheme);
                    } else {
                        //bilabial has one char
                        String palatizedBilabial = palatilise(String.valueOf(secondLastLeftChar), 'w');
                        newLeftMorpheme = leftMorpheme.substring(0, leftMorpheme.length() - 1);

                        result.add(newLeftMorpheme);
                        result.add(palatizedBilabial + "i");
                        result.add(newRightMorpheme);
                    }
                } else {
                    result.add(newLeftMorpheme);
                    result.add("i");
                    result.add(newRightMorpheme);
                }
            } else {
                result.add(newLeftMorpheme);
                result.add("wi");
                result.add(newRightMorpheme);
            }
        }
        //Vowel deletion
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
        //Coalesence
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

        else if (leftChar=='e' && rightChar=='a') {
            result.add(newLeftMorpheme);
            result.add("ya");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='e' && rightChar=='i') {
            result.add(newLeftMorpheme);
            result.add("yi");
            result.add(newRightMorpheme);
        }
        else if (leftChar=='e' && rightChar=='u') {
            result.add(newLeftMorpheme);
            result.add("yu");
            result.add(newRightMorpheme);
        }

        return result;
    }

    private String palatilise (String leftChars, char rightChar) {
        if (leftChars.equals("ph") && rightChar=='w') {
            return "tshw";
        }
        else if (leftChars.equals("bh") && rightChar=='w') {
            return "jw";
        }
        else if (leftChars.equals("b") && rightChar=='w') {
            return "tyw";
        }
        else if (leftChars.equals("p") && rightChar=='w') {
            return "tshw";
        }
        else if (leftChars.equals("m") && rightChar=='w') {
            return "nyw";
        }
        return null;
    }

}
