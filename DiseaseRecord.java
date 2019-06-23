import java.util.ArrayList;
import java.util.Random;

public class DiseaseRecord {
    private int id;
    private String url;
    private ArrayList<String> symptoms;

    public DiseaseRecord(int id, String url) {
        this.id = id;
        this.url = url;
        symptoms = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<String> symptoms) {
        this.symptoms = symptoms;
    }


    //the following toString is to print in this format (Actual tab seperated format) doid#### ph1 ph2 ... phN

    @Override
    public String toString() {
        String out = "";
        boolean lastPheno = false;

        for (String s : symptoms) {
            if (symptoms.get(symptoms.size() - 1).equals(s))
                lastPheno = true;
            if (s.indexOf("HP") != -1)
                s = s.substring(s.indexOf("HP"));
            else
                s = s.substring(s.indexOf("MP"));


            if (lastPheno)
                out += s;
            else
                out += (s + "\t");


        }
        return String.format("%s", url.substring(url.indexOf("DOID"))) + "\t" + out;
    }

// To print in the actual format with the specified number of phenotypes choosen from the actual set.
/*
   public String toString(int n) {
        String out = "";

        int r;
ArrayList<Integer> ar = new ArrayList<>();

// if the disease has less pheneos than n, then return all the phenos available.
        int bound = symptoms.size()>n ? n : symptoms.size();

        for (int i = 0; i <bound ; i++)
            ar.add(i);

        for (int i=0; i<bound; i++) {
            r = createRandom(ar);
            String s = symptoms.get(r);

            if (s.indexOf("HP") != -1)
                s = s.substring(s.indexOf("HP"));
            else
                s = s.substring(s.indexOf("MP"));

            if (i==n-1)
                out += s;
            else
                out += (s + "\t");

        }

        return String.format("%s"," F-"+url.substring(url.indexOf("DOID")))+"\t" + out;
    }
*/

   /* public String toString(Collection<DiseaseRecord> values, int n) {
        String out = "";


        DiseaseRecord randomrecord = null;
        int size = values.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int index = 0;
        for(Object obj : values)
        {
            if (index == item)
                 randomrecord= (DiseaseRecord) obj;
            index++;
        }

        int r;
        ArrayList<Integer> ar = new ArrayList<>();
        int bound = randomrecord.symptoms.size()>n ? n : randomrecord.symptoms.size();

        for (int i = 0; i <bound ; i++)
            ar.add(i);

        for (int i=0; i<bound; i++) {
            r = createRandom(ar);
            String s = randomrecord.symptoms.get(r);

            if (s.indexOf("HP") != -1)
                s = s.substring(s.indexOf("HP"));
            else
                s = s.substring(s.indexOf("MP"));

            if (i==n-1)
                out += s;
            else
                out += (s + "\t");

        }

        return String.format("%s",url.substring(url.indexOf("DOID")))+"\t" + out;
    }

*/


    public static int createRandom(ArrayList<Integer> ar) {
        Random random = new Random();
        if (ar.size() == 1)
            return 0;
        int r = ar.get(random.nextInt(ar.size()));
        ar.remove(new Integer(r));
        return r;
    }
}
