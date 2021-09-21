package za.co.mahlaza.research.grammarengine.nguni;

public class NguniVowelDetector {
    private char [] vowels = {'a', 'e', 'i', 'o', 'u'};
    private String [] labials = {"ph", "bh", "b", "p", "m"};
    private String [] alveolars = {"d", "nd", "n", "th", "nt", "t"};

    private char[] frontVowels = {'i', 'e'};
    private char[] backVowels = {'u', 'o'};
    private char[] centralVowels = {'a'};

    public boolean isVowel(char someChar) {
        return existsIn(someChar, vowels);
    }

    public boolean isLabial(String someString) {
        return existsIn(someString, labials);
    }

    public boolean isLabial(char someChar) {
        return existsIn(someChar, labials);
    }

    public boolean isAlveolar(String someString) {
        return existsIn(someString, alveolars);
    }

    public boolean isVowelPosition(char vowel, VowelPosition somePosition) {
        switch (somePosition) {
            case FRONT: {
                return existsIn(vowel, frontVowels);
            }
            case BACK: {
                return existsIn(vowel, backVowels);
            }
            case CENTER: {
                return existsIn(vowel, centralVowels);
            }
        }
        return false;
    }

    private boolean existsIn(char someChar, char[] someCharList) {
        boolean result;
        String someCharAsString = String.valueOf(someChar);
        String [] someCharListAsStrings = new String [someCharList.length];
        for (int i=0; i<someCharList.length; i++) {
            someCharListAsStrings[i] = String.valueOf(someCharList[i]);
        }
        result = existsIn(someCharAsString, someCharListAsStrings);
        return result;
    }

    private boolean existsIn(char someChar, String[] someCharList) {
        return existsIn(String.valueOf(someChar), someCharList);
    }

    private boolean existsIn(String someChar, String[] someCharList) {
        boolean result = false;
        for (int i=0; i<someCharList.length; i++) {
            if (someCharList[i].equals(someChar)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
