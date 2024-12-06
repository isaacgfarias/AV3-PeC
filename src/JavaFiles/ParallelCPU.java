package JavaFiles;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ParallelCPU {
    private final String targetWord;
    private Integer count;
    private long startTime, endTime;
    private HashMap<String, ArrayList<Integer>> results;

    public ParallelCPU(String targetWord) {
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
                    List<String> lines = new ArrayList<>();
                    String line;

                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }

                    startTime = System.currentTimeMillis();

                    int numThreads = Runtime.getRuntime().availableProcessors();
                    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
                    List<Future<Integer>> futures = new ArrayList<>();

                    int chunkSize = (lines.size() + numThreads - 1) / numThreads;
                    for (int i = 0; i < numThreads; i++) {
                        final int start = i * chunkSize;
                        final int end = Math.min(start + chunkSize, lines.size());
                        List<String> chunk = lines.subList(start, end);

                        futures.add(executor.submit(() -> {
                            int localCount = 0;
                            for (String l : chunk) {
                                String[] words = l.split("\\s+");
                                for (String word : words) {
                                    if (word.equalsIgnoreCase(targetWord)) {
                                        localCount++;
                                    }
                                }
                            }
                            return localCount;
                        }));
                    }

                    for (Future<Integer> future : futures) {
                        count += future.get();
                    }

                    executor.shutdown();
                    endTime = System.currentTimeMillis();

                } catch (IOException | InterruptedException | ExecutionException e) {
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
