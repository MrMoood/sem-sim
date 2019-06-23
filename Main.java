import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class CustomException extends Exception {
    CustomException(String msg) {
        super(msg);
    }
}

public class Main {
    public static final String PATH = "patho_pheno_with_labels.nt";
    public static HashMap<Integer, DiseaseRecord> records = new HashMap(120000);

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new FileInputStream(PATH));
        while (sc.hasNextLine()) {
            try {
                String line = sc.nextLine();
                ArrayList<String> tokens = splitter(line);
                if (tokens.size() != 3) {
                    break;
                } else if (!tokens.get(2).contains("dis_pheno_association_")) {
                    continue;
                }
                int i = tokens.get(0).indexOf("DOID_");
                if (i == -1) {
                    throw new CustomException("Expected ID when examining \"" + line + "\"");
                }
                int id = Integer.parseInt(tokens.get(0).substring(i + 5));
                if (!records.containsKey(id)) {
                    records.put(id, new DiseaseRecord(id, tokens.get(0)));
                }
                boolean foundHP = false;
                for (int j = 0; j < 2; j++) {
                    String line2 = sc.nextLine();
                    tokens = splitter(line2);
                    if (tokens.get(2).contains("HP_") || tokens.get(2).contains("MP_")) {
                        if (foundHP) {
                            throw new CustomException("Found 2 HP when examining \"" + line + "\"");
                        }
                        foundHP = true;
                        records.get(id).getSymptoms().add(tokens.get(2));
                    }
                }
                if (!foundHP) {
                    throw new CustomException("Did not find HP in first 2 lines after id \"" + line + "\"");
                }
            } catch (CustomException e) {
                //System.out.println(e.getMessage());
            }
        }
        sc.close();
        writeToFile();

    }

    /*
    // normal case
        public static void writeToFile() {

            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileOutputStream(new File("oldDB" + ".tab")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
                for (DiseaseRecord dr : records.values()) {
                    pw.println(dr.toString());
                    pw.flush();

                }


            pw.close();
        }

    */
// to print n files
    public static void writeToFile() throws FileNotFoundException {

        PrintWriter pw = null;

        pw = new PrintWriter(new FileOutputStream(new File("dis-pheno.txt")));

        for (DiseaseRecord dr : records.values()) {
            pw.println(dr.toString());
            pw.flush();

        }
        // to print  25 files where each file has n number of phenothpes for each disease.
/*
        for (int i = 1; i <= 25; i++)
        {

            pw = new PrintWriter(new FileOutputStream(new File("C:\\Users\\USER\\IdeaProjects\\create dis-pheno\\nFiles\\n"+i+".txt")));

            for (DiseaseRecord dr : records.values()) {
                pw.println(dr.toString(i));
                pw.flush();

            }
        }
*/

        pw.close();
    }

    public static ArrayList<String> splitter(String line) {
        ArrayList<String> result = new ArrayList<>();
        String s = "";
        boolean recording = false;
        for (char c : line.toCharArray()) {
            // < to ignore the end of the DB which has nemes and blah blah that we do not need here.
            if (!recording && c == '<') {
                recording = true;
            } else {
                if (c == '>') {
                    result.add(s);
                    s = "";
                    recording = false;
                } else {
                    s += c;
                }
            }
        }
        return result;
    }
}
