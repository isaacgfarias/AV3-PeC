package JavaFiles;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();

        // Executa o benchmark especificando o diretório "assets" e a palavra a ser contada
        HashMap<String, HashMap<String, ArrayList<Integer>>> results = benchmark.run("assets", "suaPalavra");

        // Exporta os resultados para um arquivo CSV
        benchmark.exportResultsToCSV("output.csv");

        // Configura os dados para os gráficos
        GraphicsFactory.data = results;

        // Inicia a geração dos gráficos
        GraphicsFactory.main(args);
    }
}
