package JavaFiles;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphicsFactory extends Application {
    public static HashMap<String, HashMap<String, ArrayList<Integer>>> data;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Análise de Desempenho");

        // Configuração dos eixos
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Algoritmo");
        yAxis.setLabel("Tempo de Execução (ms)");

        // Criação do gráfico de barras
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Comparação de Algoritmos");

        // Adicionar os dados ao gráfico
        for (String algorithm : data.keySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(algorithm);

            for (String file : data.get(algorithm).keySet()) {
                int executionTime = data.get(algorithm).get(file).get(1); // Tempo de execução
                series.getData().add(new XYChart.Data<>(file, executionTime));
            }

            barChart.getData().add(series);
        }

        // Configuração da cena e exibição
        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Inicia a aplicação JavaFX
    }
}
