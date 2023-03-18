public class SnilsVAlidatorAlternativ implements SnilsValidator{

        private static final int CORRECT_LENGTH = 11;

        @Override
        public boolean validate(String snils) {
            if (snils == null || snils.length() != CORRECT_LENGTH) {
                return false;
            }

            // Проверяем, что все символы являются цифрами
            for (char c : snils.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }

            // Рассчитываем контрольную сумму
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += (snils.charAt(i) - '0') * (9 - i);
            }
            int checkDigit = Integer.parseInt(snils.substring(9, 11));
            int expectedCheckDigit = sum < 100 ? sum : sum % 101;
            return checkDigit == expectedCheckDigit;
        }
    }
