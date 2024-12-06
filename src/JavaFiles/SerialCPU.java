package JavaFiles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SerialCPU {
    private final String targetWord;
    private Integer count;
    private long startTime, endTime;
    private HashMap<String, ArrayList<Integer>> results;

    public SerialCPU(String targetWord) {
        this.targetWord = targetWord.toLowerCase();
        this.results = new HashMap<>();
    }

    public HashMap<String, ArrayList<Integer>> count(File assets) {
        if (!assets.exists() || !assets.isDirectory()) {
            System.out.println("O diretório 'assets' não existe ou não é válido.");
            return null;
        }

        for (File file : assets.listFiles()) {
            count = 0;
            if (file.isFile()) { // Garante que não é um subdiretório
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    startTime = System.currentTimeMillis();
                    while ((line = br.readLine()) != null) {
                        String[] words = line.split("\\s+");
                        for (String word : words) {
                            if (word.equalsIgnoreCase(targetWord)) {
                                count++;
                            }
                        }
                    }
                    endTime = System.currentTimeMillis();
                } catch (IOException e) {
                    System.out.println("Erro ao processar o arquivo: " + file.getName());
                    e.printStackTrace();
                }
            }

            results.put(file.getName(), new ArrayList<>() {{
                add(count);
                add((int) (endTime - startTime));
            }});
        }
        return this.results;
    }
}
