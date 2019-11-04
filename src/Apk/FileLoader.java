package Apk;

import Apk.comparators.AirplaneCodeKey;
import Apk.comparators.FlightCodeKey;
import Apk.comparators.FlightPriorityKey;
import Apk.comparators.RunwayTypeKey;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import structures.PairingHeap;
import structures.SplayTree;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileLoader {

    private File rootDirectory;
    private List<File> runwayTypeDirs;
    private File arrivedFlightsFile;
    private File allAirplanesFile;
    private File dateAndTimeFile;

    private Airport airport;

    private SplayTree<AirplaneCodeKey, Airplane> allAirplanes;

    public FileLoader(File rootDirectory) {
        this.airport = null;
        this.allAirplanes = new SplayTree<>();
        this.rootDirectory = rootDirectory;
        File[] files = rootDirectory.listFiles();
        if (files == null) {
            return;
        }
        this.runwayTypeDirs = new ArrayList<>(files.length - 3);
        for (File file : files) {
            if (file.isFile()) {
                switch (file.getName()) {
                    case FileSaver.ALL_PLANES : {
                        allAirplanesFile = file;
                        break;
                    }
                    case FileSaver.ARRIVED_FLIGHTS : {
                        arrivedFlightsFile = file;
                        break;
                    }
                    case FileSaver.DATE_TIME : {
                        dateAndTimeFile = file;
                        break;
                    }
                }
            }
            else {
                this.runwayTypeDirs.add(file);
            }
        }
        Collections.shuffle(runwayTypeDirs); // znizenie sance na zdegenerovanie splayStromu
    }

    private LocalDateTime formatDateTime(String strDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(strDateTime, formatter);
    }

    public LocalDateTime loadDateTime() throws IOException, IllegalArgumentException {
        Scanner scanner = new Scanner(dateAndTimeFile);
        if (scanner.hasNextLine()) {
            String dateStr = scanner.nextLine();
            return formatDateTime(dateStr);
        }
        else {
            throw new IllegalArgumentException("LocalDateTime is not in the file: " + FileSaver.DATE_TIME);
        }
    }

    public SplayTree<AirplaneCodeKey, Airplane> loadAllAirplanes() throws IOException, IllegalArgumentException {
        allAirplanes = new SplayTree<>();
        Scanner scanner = new Scanner(allAirplanesFile);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            Airplane airplane = new Airplane(nextLine.split(","));
            allAirplanes.insert(new AirplaneCodeKey(airplane), airplane);

        }
        return allAirplanes;
    }

    private Flight loadFlight(String code) {
        AirplaneCodeKey codeKey = new AirplaneCodeKey(new Airplane(code));
        Airplane airplane = allAirplanes.find(codeKey); // ziskam referenciu na lietadlo, ktore ma vyplnene vsetky atributy
        return new Flight(airplane);
    }

    public SplayTree<FlightCodeKey, Flight> loadArrivedFlights() throws IOException, IllegalArgumentException{
        SplayTree<FlightCodeKey, Flight> arrivedFlights = new SplayTree<>();
        Scanner scanner = new Scanner(arrivedFlightsFile);
        while (scanner.hasNextLine()) {
            String[] attributes = scanner.nextLine().split(",");
            String code = attributes[0];
            Flight flight = loadFlight(code);
            LocalDateTime arrivalTime = formatDateTime(attributes[1]);
            flight.setArrival(arrivalTime);
            arrivedFlights.insert(new FlightCodeKey(flight), flight);
        }
        return arrivedFlights;
    }

    public SplayTree<RunwayTypeKey, RunwayType> loadRunways(Airport airport) throws IOException{
        this.airport = airport;
        SplayTree<RunwayTypeKey, RunwayType> runwayTypes = new SplayTree<>();

        for (File runwayTypeDir : runwayTypeDirs) {

            int length = Integer.parseInt(runwayTypeDir.getName()); // dlzka drahy je reprezentovana menom priecinku
            File[] runwayFiles = runwayTypeDir.listFiles();
            if (runwayFiles == null) {
                continue;
            }
            RunwayType runwayType = loadRunwayType(length, runwayFiles);
            runwayTypes.insert(new RunwayTypeKey(runwayType), runwayType);
        }
        return runwayTypes;
    }

    /**
     * Nacitam cely typ leteckej drahy aj zo vsetkymi referenciami
     */
    private RunwayType loadRunwayType(int length, File[] runwayFiles) throws IOException {
        SplayTree<FlightCodeKey, Flight> waitingFlights = new SplayTree<>();
        PairingHeap<FlightPriorityKey, Flight> waitingQueue = new PairingHeap<>();
        ArrayList<Runway> runways = new ArrayList<>(runwayFiles.length - 1);
        LinkedList<Integer> freeRunways = new LinkedList<>();
        RunwayType runwayType = new RunwayType(length, waitingFlights, waitingQueue, runways, freeRunways, airport);
        for (File file : runwayFiles) {
            String fileName = file.getName();
            if (fileName.equals(FileSaver.WAITING_FLIGHTS)) { // jeden subor obsahuje vsetky cakajuce lietadla na dany typ drahy
                loadWaitingFlights(file, waitingFlights, waitingQueue, runwayType);
            }
            else {
                // subor je letecka draha
                int id = Integer.parseInt(fileName.substring(0, fileName.indexOf(".csv")));
                Runway runway = loadRunway(file, id, length);
                runways.add(id, runway);
            }
        }
        return runwayType;
    }

    /**
     * Nacita lietadla ktore cakaju na konkretny typ drahy. Lietadla zaroven umiestnuje medzi vsetky cakajuce lietadla na letisku.
     */
    private void loadWaitingFlights(File waitingFlightsFile, SplayTree<FlightCodeKey, Flight> waitingFlights,
                                    PairingHeap<FlightPriorityKey, Flight> waitingQueue, RunwayType runwayType) throws IOException{
        Scanner scanner = new Scanner(waitingFlightsFile);
        while (scanner.hasNextLine()) {
            String[] attributes = scanner.nextLine().split(",");
            String code = attributes[0];

            Flight flight = loadFlight(code); // nacitame let
            int priority = Integer.parseInt(attributes[1]);
            flight.setPriority(priority); // pridame prioritu
            LocalDateTime arrivalTime = formatDateTime(attributes[2]);
            flight.setArrival(arrivalTime); // pridame cas priletu

            LocalDateTime requestTime = formatDateTime(attributes[3]);
            flight.setRunwayRequest(requestTime); // pridame cas poziadania o drahu
            waitingFlights.insert(new FlightCodeKey(flight), flight);

            flight.setRunwayType(runwayType); // priradime referenciu na typ drahy

            //v tomto bode ma lietadlo zadefinovane vsetky potrebne atributy
            waitingFlights.insert(new FlightCodeKey(flight), flight); // pridame do cakajucich letov na drahe
            waitingQueue.insert(new FlightPriorityKey(flight), flight); // pridame do prioritneho frontu na drahe
            airport.addFlightToWaiting(flight); // pridame do vsetkych cakajucich letov
        }
    }

    private Runway loadRunway(File runwayFile, int id, int length) throws IOException{
        Scanner scanner = new Scanner(runwayFile);
        
//        while (scanner.hasNextLine()) {
//            String[] attributes = scanner.nextLine().split(",");
//            String code = attributes[0];
//
//            Flight flight = loadFlight(code); // nacitame let
//            int priority = Integer.parseInt(attributes[1]);
//            flight.setPriority(priority); // pridame prioritu
//            LocalDateTime arrivalTime = formatDateTime(attributes[2]);
//            flight.setArrival(arrivalTime); // pridame cas priletu
//
//            LocalDateTime requestTime = formatDateTime(attributes[3]);
//            flight.setRunwayRequest(requestTime); // pridame cas poziadania o drahu
//            waitingFlights.insert(new FlightCodeKey(flight), flight);
//
//            flight.setRunwayType(runwayType); // priradime referenciu na typ drahy
//
//            //v tomto bode ma lietadlo zadefinovane vsetky potrebne atributy
//            waitingFlights.insert(new FlightCodeKey(flight), flight); // pridame do cakajucich letov na drahe
//            waitingQueue.insert(new FlightPriorityKey(flight), flight); // pridame do prioritneho frontu na drahe
//            airport.addFlightToWaiting(flight); // pridame do vsetkych cakajucich letov
//        }


        LinkedList<Flight> flightHistory = new LinkedList<>();
        Runway runway = new Runway(id, length);
        return runway;
    }



}
