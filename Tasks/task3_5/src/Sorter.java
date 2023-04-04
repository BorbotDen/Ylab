import java.io.*;
import java.util.*;

public class Sorter {
    private final int MAX_ROWS = 5_000_000;
    private final String SORTFILE_DIR = "sortedFile.txt";
    private List<Long> numbers = new ArrayList<>();
    private List<File> tempFiles = new ArrayList<>();

    public File sortFile(File dataFile) throws IOException {
        readFile(dataFile);
        File sortedFile = margeTempFiles();
        deleteTempFiles();
        return sortedFile;
    }

    //Удаляем временные файлы
    private void deleteTempFiles() {
        for (File file : tempFiles) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    //Делаем Marge sort из временных файлов и запись в один файл
    private File margeTempFiles() {
        BufferedReader[] readers = new BufferedReader[tempFiles.size()];
        for (int i = 0; i < tempFiles.size(); i++) {
            try {
                readers[i] = new BufferedReader(new FileReader(tempFiles.get(i)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        File sortedFile = new File(SORTFILE_DIR);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sortedFile))) {
            Queue<IndexElement> firstElements = new PriorityQueue<>();
            for (int i = 0; i < readers.length; i++) {
                String line = readers[i].readLine();
                if (line != null) {
                    Long value = Long.valueOf(line);
                    firstElements.add(new IndexElement(i, value));
                }
            }
            while (!firstElements.isEmpty()) {
                IndexElement element = firstElements.poll();
                int index = element.getIndex();
                long value = element.getValue();
                writer.write(String.valueOf(value));
                writer.newLine();
                String line = readers[index].readLine();
                if (line != null) {
                    value = Long.valueOf(line);
                    firstElements.add(new IndexElement(index, value));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            for (BufferedReader reader : readers) {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sortedFile;
    }

    //Читаем файл и добавляем список, при превышение MAX_ROWS выгружаем в файл
    private void readFile(File dataFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            int rowsCount = 0;
            while ((line = reader.readLine()) != null) {
                numbers.add(Long.valueOf(line));
                rowsCount++;
                if (rowsCount >= MAX_ROWS) {
                    saveValuesToFile();
                    rowsCount = 0;
                }
            }
            saveValuesToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Сортируем список и создаем файл
    private void saveValuesToFile() {
        Collections.sort(numbers);
        File output = new File("splitFile" + (tempFiles.size() + 1) + ".txt");
        tempFiles.add(output);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(output));
            for (Long line : numbers) {
                writer.write(String.valueOf(line));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    numbers.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
