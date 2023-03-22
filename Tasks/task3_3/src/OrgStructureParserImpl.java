import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrgStructureParserImpl implements OrgStructureParser {
    private final Map<Long, Employee> bossesTree = new HashMap<>();

    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        Employee result = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                Long id = (long) Integer.parseInt(fields[0]);
                String name = fields[2];
                String position = fields[3];
                Long bossId;
                if (fields[1].equals("")) {
                    bossId = null;
                    result = new Employee(id, bossId, name, position);
                    addChanges(result);
                } else {
                    bossId = (long) Integer.parseInt(fields[1]);
                    Employee employee = new Employee(id, bossId, name, position);
                    if (!bossesTree.containsKey(bossId)) { //создать нового боса если еще его нет в Map
                        bossesTree.put(bossId, new Employee(bossId));
                    }
                    employee.setBoss(bossesTree.get(bossId));
                    employee.getBoss().getSubordinate().add(employee);
                    addChanges(employee);
                }
            }
        }
        return result;
    }

    //Добовляем существующий список к employee и перезаписываем
    public void addChanges(Employee employee) {
        Long id = employee.getId();
        if (bossesTree.containsKey(id)) {
            employee.getSubordinate().addAll(bossesTree.get(id).getSubordinate());
        }
        bossesTree.put(id, employee);
    }

}
