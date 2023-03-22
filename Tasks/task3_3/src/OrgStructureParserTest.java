import java.io.File;
import java.io.IOException;

public class OrgStructureParserTest {
    public static void main(String[] args) throws IOException {
        OrgStructureParser org = new OrgStructureParserImpl();
        System.out.println(org.parseStructure(new File("test3_3.csv")));
        System.out.println();
        System.out.println(org.parseStructure(new File("test3_3.csv")).getSubordinate());
    }
}