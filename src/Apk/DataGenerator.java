package Apk;

import java.time.LocalDateTime;
import java.util.Random;

public class DataGenerator {

    private int counter;
    private int maxRunwayLength;
    private Random random;

    public DataGenerator() {
        this.counter = 0;
    }

    public void setMaxRunwayLength(int maxRunwayLength) {
        this.maxRunwayLength = maxRunwayLength;
        this.random = new Random();
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
        String code = generateRandomString(4) + counter;
        counter++;
        int runwayLength = random.nextInt(maxRunwayLength);
        Airplane airplane = new Airplane(type, code, runwayLength);
        return new Flight(airplane);
    }

    public int randomNumber(int upperBound) {
        return random.nextInt(upperBound);
    }

    public LocalDateTime updateDatetime(LocalDateTime dateTime) {
        double chanceOfUpdate = 0.2;
        if (random.nextDouble() < chanceOfUpdate) {
            int minutes = random.nextInt(10) + 1;
            return dateTime.plusMinutes(minutes);
        }
        else {
            return dateTime;
        }
    }
}
