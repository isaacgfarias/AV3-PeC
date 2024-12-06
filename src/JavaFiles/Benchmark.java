package JavaFiles;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Benchmark {
    HashMap<String, HashMap<String, ArrayList<Integer>>> results;

    public Benchmark() {}

    public HashMap<String, HashMap<String, ArrayList<Integer>>> run(String path, String targetWord) {
        SerialCPU sCPU = new SerialCPU(targetWord);
        ParallelCPU pCPU = new ParallelCPU(targetWord);
        ParallelGPU pGPU = new ParallelGPU(targetWord);

        File assets = new File(path);

        HashMap<String, ArrayList<Integer>> SerialCPUResults = sCPU.count(assets);
        HashMap<String, ArrayList<Integer>> ParallelCPUResults = pCPU.count(assets);
        HashMap<String, ArrayList<Integer>> ParallelGPUResults = pGPU.count(assets);

        results = new HashMap<>() {{
            put("SerialCPU", SerialCPUResults);
            put("ParallelCPU", ParallelCPUResults);
            put("ParallelGPU", ParallelGPUResults);
        }};

        return results;
    }

    public void exportResultsToCSV(String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("Algorithm,File,WordCount,ExecutionTime(ms)\n");
            for (String algorithm : results.keySet()) {
                for (String file : results.get(algorithm).keySet()) {
                    ArrayList<Integer> data = results.get(algorithm).get(file);
                    writer.write(String.format("%s,%s,%d,%d\n", algorithm, file, data.get(0), data.get(1)));
                }
            }
            System.out.println("Resultados exportados para: " + outputFile);
        } catch (IOException e) {
            System.out.println("Erro ao exportar resultados: " + e.getMessage());
        }
    }
}
