import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConversorDeMoedasComAPI {

    private static final String API_KEY = "7a11b7ee00a8497b2be8745a";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    private static final Map<Integer, String> moedas = new HashMap<>();

    static {
        moedas.put(1, "ARS"); // Peso Argentino
        moedas.put(2, "USD"); // Dólar
        moedas.put(3, "EUR"); // Euro
        moedas.put(4, "BRL"); // Real
        moedas.put(5, "CNY"); // Yuan
        moedas.put(6, "GBP"); // Libras
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== CONVERSOR DE MOEDAS COM API =====");
        System.out.println("Moedas disponíveis:");
        for (Map.Entry<Integer, String> entry : moedas.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

        System.out.print("Escolha a moeda de origem (1-6): ");
        int origem = scanner.nextInt();

        System.out.print("Escolha a moeda de destino (1-6): ");
        int destino = scanner.nextInt();

        System.out.print("Digite o valor a ser convertido: ");
        double quantidade = scanner.nextDouble();

        String fromCurrency = moedas.get(origem);
        String toCurrency = moedas.get(destino);

        if (fromCurrency == null || toCurrency == null) {
            System.out.println("Moeda inválida selecionada.");
        } else {
            double valorConvertido = converterMoeda(fromCurrency, toCurrency, quantidade);
            System.out.printf("Resultado: %.2f %s = %.2f %s\n", quantidade, fromCurrency, valorConvertido, toCurrency);
        }

        scanner.close();
    }

    public static double converterMoeda(String from, String to, double amount) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = BASE_URL + API_KEY + "/pair/" + from + "/" + to;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            // Gambiarra segura corrigida:
            String taxaStr = body.split("\"conversion_rate\":")[1].split("[,}]")[0].trim();
            double rate = Double.parseDouble(taxaStr);

            return amount * rate;

        } catch (Exception e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
            return 0.0;
        }
    }
}