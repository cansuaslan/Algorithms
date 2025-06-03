import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile(varName + "\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }


    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile(varName + "\\s*=\\s*([0-9]*\\.?[0-9]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return 0.0;
    }


    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Pattern p = Pattern.compile(varName + "\\s*=\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fileContent);
        if (m.find()) {
            return new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        }
        return new Point(0, 0);
    }


    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */

    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();

        //Pattern pattern = Pattern.compile("train_line_name\\s*=\\s*\"([^\"]+)\"\\s+train_line_stations\\s*=\\s*\\((.*?)\\)", Pattern.DOTALL);
        Pattern pattern = Pattern.compile("train_line_name\\s*=\\s*\"([^\"]+)\"\\s+train_line_stations\\s*=\\s*((?:\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*)+)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()) {
            String lineName = matcher.group(1);
            String stationsStr = matcher.group(2).trim();

            List<Station> stationList = new ArrayList<>();

            Pattern stationPattern = Pattern.compile("(\\d+)\\s*,\\s*(\\d+)");
            Matcher stationMatcher = stationPattern.matcher(stationsStr);
            int i = 1;
            while (stationMatcher.find()) {
                int x = Integer.parseInt(stationMatcher.group(1));
                int y = Integer.parseInt(stationMatcher.group(2));
                Point point = new Point(x, y);
                Station station = new Station(point,  lineName + " Line Station " + i);
                i++;
                stationList.add(station);
            }
            TrainLine trainLine = new TrainLine(lineName, stationList);
            trainLines.add(trainLine);
        }

        return trainLines;
    }


    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */

    private String readFile(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void readInput(String filename) {
        String fileContent = readFile(filename);

        numTrainLines = getIntVar("num_train_lines", fileContent);
        averageTrainSpeed = getDoubleVar("average_train_speed", fileContent) * 1000/60;

        Point start = getPointVar("starting_point", fileContent);
        Point destination = getPointVar("destination_point", fileContent);

        startPoint = new Station(start, "Starting Point");
        destinationPoint = new Station(destination, "Final Destination");
        lines = getTrainLines(fileContent);
    }
}