package Apk;

import java.util.List;
import java.util.Random;

public class DataGenerator {

    private int code;
    private int maxRunwayLength;

    public DataGenerator() {
        this.code = 0;
    }

    public void setMaxRunwayLength(int maxRunwayLength) {
        this.maxRunwayLength = maxRunwayLength;
    }

    /**
     * Zdroj: https://www.baeldung.com/java-random-string
     * @return vrati nahodny string
     */
    private String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public Flight randomFlight() {
        String type = generateRandomString(5);
        Random random = new Random();
        int runwayLength = random.nextInt(maxRunwayLength);
        Airplane airplane = new Airplane(type, Integer.toString(code), runwayLength);
        int priority = random.nextInt(10) + 1;
        Flight flight = new Flight(airplane);
        code++;
        return flight;
    }


}
