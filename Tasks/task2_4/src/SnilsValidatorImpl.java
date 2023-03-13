public class SnilsValidatorImpl implements SnilsValidator {
    private static final int CORRECT_LENGHT = 11;
    private static final int UNIQUE_NUMBERS = 9;
    private static final int HASH_CONST = 100;
    private static final int DEFAULT_VALUE = 0;

    private int[] snilsNumbers;

    @Override
    public boolean validate(String snils) {
        if (snils.length() == CORRECT_LENGHT) {
            this.snilsNumbers = new int[snils.length()];
            for (int i = 0; i < CORRECT_LENGHT; i++) {
                char character = snils.charAt(i);
                boolean isDigit = Character.isDigit(character);
                if (isDigit) {
                    int num = Character.digit(snils.charAt(i), 10);
                    snilsNumbers[i] = num;
                } else {
                    return false;
                }
            }
            return numberIsCorrect();
        }
        return false;
    }

    private boolean numberIsCorrect() {
        int sum = 0;
        for (int i = 0; i < UNIQUE_NUMBERS; i++) {
            sum += snilsNumbers[i] * (UNIQUE_NUMBERS - i);
        }
        return checkHash(sum);
    }

    private boolean checkHash(int sum) {
        int givenHash = snilsNumbers[CORRECT_LENGHT - 2] * 10 + snilsNumbers[CORRECT_LENGHT - 1];
        return calculateHash(sum) == givenHash;
    }

    private int calculateHash(int sum) {
        int result = sum;
        if (sum == HASH_CONST) {
            result = DEFAULT_VALUE;
        } else if (sum > HASH_CONST) {
            result = sum % (HASH_CONST + 1);
            if (result == HASH_CONST) {
                result = DEFAULT_VALUE;
            }
        }
        return result;
    }
}