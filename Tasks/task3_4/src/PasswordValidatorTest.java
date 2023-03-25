public class PasswordValidatorTest {
    public static void main(String[] args) {
        String login = "arganaft_12";
        String password = "MyBD_1999";
        String confirmPassword = "MyBD_1999";
        boolean valid = PasswordValidator.validationDataForRegistration(login,password,confirmPassword);
        System.out.println(valid);
    }
}