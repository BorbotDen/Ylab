public class PasswordValidator {

    public static boolean validationDataForRegistration(String login, String password, String confirmPassword) {
        try {
            checkLogin(login);
            checkPassword(password, confirmPassword);
            return true;
        } catch (WrongLoginException | WrongPasswordException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static void checkPassword(String password, String confirmPassword) throws WrongPasswordException {
        if (!password.matches("[a-zA-Z0-9_]*")) {
            throw new WrongPasswordException("Пароль содержит недопустимые символы");
        }
        if (password.length() > 20) {
            throw new WrongPasswordException("Пароль слишком длинный");
        }
        if (!password.equals(confirmPassword)) {
            throw new WrongPasswordException("Пароль и подтверждение не совпадают");
        }
    }

    private static void checkLogin(String login) throws WrongLoginException {
        if (!login.matches("[a-zA-Z0-9_]*")) {
            throw new WrongLoginException("Логин содержит недопустимые символы");
        }
        if (login.length() > 20) {
            throw new WrongLoginException("Логин слишком длинный");
        }

    }


}
