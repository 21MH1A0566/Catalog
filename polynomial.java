import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;

public class polynomial {
    public static BigInteger convertToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }
    public static BigInteger lagrangeInterpolation(List<BigInteger> x, List<BigInteger> y, int k) {
        BigInteger c = BigInteger.ZERO;
        
        for (int i = 0; i < k; i++) {
            BigInteger term = y.get(i);
            BigInteger denominator = BigInteger.ONE;
            BigInteger numerator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(x.get(j).negate());
                    denominator = denominator.multiply(x.get(i).subtract(x.get(j))); // Denominator (xi - xj)
                }
            }
            term = term.multiply(numerator).divide(denominator);
            c = c.add(term);
        }
        
        return c;
    }
    public static BigInteger solveFromJson(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            JSONObject json = new JSONObject(new JSONTokener(reader));
            JSONObject keys = json.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            List<BigInteger> xValues = new ArrayList<>();
            List<BigInteger> yValues = new ArrayList<>();
            for (String key : json.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject point = json.getJSONObject(key);
                    int base = point.getInt("base");
                    String value = point.getString("value");

                    BigInteger x = new BigInteger(key);
                    BigInteger y = convertToDecimal(value, base);

                    xValues.add(x);
                    yValues.add(y);

                    if (xValues.size() == k) break; // Stop when k values are collected
                }
            }
            return lagrangeInterpolation(xValues, yValues, k);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigInteger.ZERO;
    }

    public static void main(String[] args) {
        System.out.println("Secret from first JSON file: " + solveFromJson("testcase1.json"));
        System.out.println("Secret from second JSON file: " + solveFromJson("testcase2.json"));
    }
}
