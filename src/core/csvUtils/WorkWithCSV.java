package core.csvUtils;

/**
 * Created by serdyuk on 6/12/16.
 */

import com.opencsv.CSVWriter;
import core.configs.ConfigLoader;
import core.random.RandomUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Method for work with CSV file.
 * ExampleSite: http://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
 * ExampleSite: http://www.csvreader.com/java_csv_samples.php
 */

public class WorkWithCSV {
    RandomUtils randomUtils = new RandomUtils();
    ConfigLoader config = new ConfigLoader();
    String saveFile = config.REPORTS_DIR + "/";
    String formatForFile = ".csv";          //@TODO look down
    String saveToCSV = saveFile + randomUtils.getDateAndTime() + formatForFile;

    String tesDataReadFileName = "/testData";
    String testDataCSV = config.REPORTS_DIR + tesDataReadFileName + formatForFile;


    /**
     * @Method save in CSV File
     * This method create csv file with UniqName from incoming parameters
     * Add to already exist file new Data if we need
     */

    public void saveInCSV(String fileTestName, String email, String location, String userID, String siteName, String userAutologin) {

        String createAndSaveAndAddToCSV = saveFile + fileTestName + formatForFile;
        boolean alreadyExists = new File(createAndSaveAndAddToCSV).exists();

        try {
            FileWriter writer = new FileWriter(createAndSaveAndAddToCSV, true);
            if (!alreadyExists) {
                writer.append("Email:");
                writer.append(',');
                writer.append("location:");
                writer.append(',');
                writer.append("User ID");
                writer.append(",");
                writer.append("Autologin");
                writer.append(",");
                writer.append("SiteName");
                writer.append("\n");
            }

            writer.append(email);
            writer.append(',');
            writer.append(location);
            writer.append(',');
            writer.append(userID);
            writer.append(',');
            writer.append(userAutologin);
            writer.append(',');
            writer.append(siteName);
            writer.append("\n");

            //generate whatever data you want

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create CSV file if not exist;
     *
     * @param name of csv file, without format
     * @return absolute path of csv file;
     */
    public String createCsv(String name) {
        String csvPath = saveFile + name + formatForFile;
        new File(csvPath);
        return csvPath;
    }

    public String createAnyFile(String path, String name, String format) {
        String anyFile = path + name + format;
        new File(anyFile);
        return anyFile;
    }

    /**
     * Write array data to csv file
     *
     * @param csvFile Absolute path to csv file
     * @param data    ArrayList of data
     * @throws IOException
     */
    public void writeToCsv(String csvFile, HashMap<String, String> data) throws IOException {

        CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true), ',', CSVWriter.NO_QUOTE_CHARACTER);
        String[] array = data.values().toArray(new String[data.size()]);
        writer.writeNext(array);
        writer.close();
    }

    public void writeToCsv(String csvFile, ArrayList<String> data) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true), ',', CSVWriter.NO_QUOTE_CHARACTER);
        String[] array = data.toArray(new String[data.size()]);
        writer.writeNext(array);
        writer.close();
    }

    /**
     * @method Read from CSV for Data provider
     * User set site, location, traff sourse, gender, email, freePaid params in his csv file for registration user.
     * test run and get parameters for registration from csv file.
     * 2nd method take absolute path & name for csv file
     * Methods return String array
     * In site column user add m. or wwww. dependence from site version
     * In CSV File separator must be comma ","
     */
    public Iterator<Object[]> csvReader() throws IOException {
        Set<Object[]> result = new HashSet<Object[]>();
        for (final String line : Files.readAllLines(Paths.get(config.REPORTS_DIR + "/" + "regTest.csv"),
                StandardCharsets.UTF_8)) {

            result.add(new Object[]{
                    "https://" + line.split(",")[0].replaceAll("\\s+", ""),
                    line.split(",")[1],
                    line.split(",")[2].toLowerCase(),
                    line.split(",")[3].toLowerCase(),
                    line.split(",")[4].toLowerCase(),
                    line.split(",")[5].toLowerCase()}); //site, location, traff sourse, gender, email, freePaid
        }
        System.out.println(result.size());
        return result.iterator();
    }

    public Iterator<Object[]> csvReader(String absolutePath) throws IOException {
        Set<Object[]> result = new HashSet<>();
        for (String line : Files.readAllLines(Paths.get(absolutePath), StandardCharsets.UTF_8)) {
            Collections.addAll(result, line.split(","));
        }
        System.out.println(result.size());
        return result.iterator();
    }

    public Iterator<Object[]> csvGenerateUsers(String absolutePath) throws IOException {
        Set<Object[]> result = new HashSet<Object[]>();
        for (final String line : Files.readAllLines(Paths.get(absolutePath),
                StandardCharsets.UTF_8)) {

            result.add(new Object[]{
                    "https://" + line.split(",")[0],
                    line.split(",")[1],
                    line.split(",")[2].toLowerCase(),
                    line.split(",")[3].toLowerCase(),
                    line.split(",")[4].toLowerCase(),
                    line.split(",")[5].toLowerCase(),
                    line.split(",")[6].toLowerCase(),
//                    line.split(",")[7].toUpperCase()
            }); //site, location, traff sourse, gender, email, freePaid, web/Mob
        }
        System.out.println(result.size());
        return result.iterator();
    }
}
