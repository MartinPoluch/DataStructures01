package Apk;

import Apk.comparators.AirplaneCodeKey;
import Apk.comparators.FlightCodeKey;
import Apk.comparators.RunwayTypeKey;
import structures.SplayTree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class FileSaver {

    private File rootDirectory;

    public FileSaver(File locationOfDirectory) throws IllegalArgumentException{
        String directoryName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        this.rootDirectory = new File(locationOfDirectory.getPath() + "/" + directoryName);
        if (! rootDirectory.mkdir()) {
            throw new IllegalArgumentException("Cannot create directory: " + locationOfDirectory.getPath() + "/" + directoryName);
        }
    }

    public void save(SplayTree<AirplaneCodeKey, Airplane> airplanes) throws IOException {
        File allAirplanesFile = new File(rootDirectory.getPath() + "/allAirplanes.csv");
        if (allAirplanesFile.createNewFile()) {
            airplanes.levelOrder(allAirplanesFile);
        }
    }

    public void save(SplayTree<FlightCodeKey, Flight> flights, String nameOfFile) throws IOException {
        File flightsFile = new File(rootDirectory.getPath() + "/" + nameOfFile + ".csv");
        if (flightsFile.createNewFile()) {
            flights.levelOrder(flightsFile);
        }
    }

    public void save(LocalDateTime actualDateTime) throws IOException{
        File dateTimeFile = new File(rootDirectory.getPath() + "/dateTime.csv");
        if (dateTimeFile.createNewFile()) {
            FileWriter writer = new FileWriter(dateTimeFile);
            String dateTime = actualDateTime.toLocalDate() + " " + actualDateTime.toLocalTime();
            writer.append(dateTime);
            writer.flush();
            writer.close();
        }
    }

    public void save(File numberOfRunways) throws IOException {
        Path source = Paths.get(numberOfRunways.getPath());
        OutputStream destination = new FileOutputStream(rootDirectory.getPath() + "/" + source.getFileName());
        Files.copy(source, destination); // urobim kopiu komfiguracneho suboru
    }

    public void save(RunwayType runwayType) throws IOException{
        File runwayTypeDir = new File(rootDirectory.getPath() + "/" + runwayType.getLength());
        if (runwayTypeDir.mkdir()) {
            for (Runway runway : runwayType.getRunways()) {
                File runwayFile = new File(runwayTypeDir.getPath() + "/" + runway.getId() + ".csv");
                FileWriter writer = new FileWriter(runwayFile);
                for  (Flight flight : runway.getFlightsHistory()) {
                    writer.append(flight.toString());
                }
                writer.flush();
                writer.close();
            }
        }
    }

}
