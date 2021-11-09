import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap; 


public class join {
    public static void main(String[] args) throws IOException {
        String a_filename;
        String a_key_name;
        String b_filename;
        String b_key_name;
        String join_type;
        String o_filename;
        if (args.length == 6) {
            a_filename = args[0];
            a_key_name = args[1];
            b_filename = args[2];
            b_key_name = args[3];
            join_type = args[4];
            o_filename = args[5];
        }
        else {
            // System.out.println("Invalid argument(s)");
            a_filename = "nation.tbl";
            a_key_name = "REGIONKEY";
            b_filename = "region.tbl";
            b_key_name = "REGIONKEY";
            join_type = "NESTED_LOOP";
            o_filename = "output.tbl";
            // return;
        }
        // LOAD LEFT TABLE
        String a_header;
        HashMap<String, Integer>  a_keys = new HashMap<String,Integer>();
        List<List<String>> a_records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(a_filename))) {
            String line;
            a_header = br.readLine();
            Integer count = 0;
            for (String a_key : a_header.split("\\|")) {
                a_keys.put(a_key,count);
                count ++;
            }
            while((line = br.readLine()) != null) {
                String[] values = line.split("\\|");
                a_records.add(Arrays.asList(values));
            }
        }          

        // LOAD RIGHT TABLE
        String b_header;
        HashMap<String, Integer>  b_keys = new HashMap<String,Integer>();
        List<List<String>> b_records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(b_filename))) {
            String line;
            b_header = br.readLine();
            Integer count = 0;
            for (String b_key : b_header.split("\\|")) {
                b_keys.put(b_key,count);
                count ++;
            }
            while((line = br.readLine()) != null) {
                String[] values = line.split("\\|");
                b_records.add(Arrays.asList(values));
            }
        }

        // CHECK KEY MATCHING
        Integer a_key_index = a_keys.get(a_key_name);
        Integer b_key_index = b_keys.get(b_key_name);
        if (a_key_index == null || b_key_index == null) {
            System.out.println("Invalid join key(s)");
            return;
        }

        // CREATE AND OPEN OUTPUT FILE
        File oFile = new File(o_filename);
        oFile.createNewFile();
        FileWriter fw = new FileWriter(o_filename);

        // WRITE HEADER
        fw.write(a_header);
        fw.write(b_header);
        fw.write("\n");
        
        // BENCHMARK TIME BEGIN
        long start_time = System.nanoTime();

        // JOIN
        if (join_type.equals("NESTED_LOOP")) {
            for (List<String> a_row : a_records) {
                for (List<String> b_row : b_records) {
                    if (a_row.get(a_key_index).equals(b_row.get(b_key_index))) {
                        // WRITE ROW
                        for (String a_item : a_row) {
                            fw.write(String.format("%s|", a_item));
                        }
                        for (String b_item : b_row) {
                            fw.write(String.format("%s|", b_item));
                        }
                        fw.write("\n");
                    }
                }
            }
        }
        else if (join_type.equals("HASH")) {
            // CREATE HASH RECORD
            HashMap<String, List<Integer>> hash_index = new HashMap<String, List<Integer>>();
            for (int i = 0; i < a_records.size(); i++) {
                if (hash_index.get(a_records.get(i).get(a_key_index)) == null) {
                    List<Integer> index_list = new ArrayList<Integer>();
                    index_list.add(i);
                    hash_index.put(a_records.get(i).get(a_key_index), index_list);
                }
                else {
                    hash_index.get(a_records.get(i).get(a_key_index)).add(i);
                }
            }
            
            // FIND MATCH
            for (List<String> b_row : b_records) {
                List<Integer> match_index_list = hash_index.get(b_row.get(b_key_index));
                if (match_index_list != null) {
                    for (Integer match_i : match_index_list) {
                        // WRITE ROW
                        for (String a_item : a_records.get(match_i)) {
                            fw.write(String.format("%s|", a_item));
                        }
                        for (String b_item : b_row) {
                            fw.write(String.format("%s|", b_item));
                        }
                        fw.write("\n"); 
                    }

                }
            }
        }
        else {
            System.out.println("Invalid join type");
            fw.close();
            return;
        }
        // BENCHMARK TIME END
        long end_time = System.nanoTime();

        // END PROCESS
        fw.close();

        // OUTPUT BENCHMARK RESULT
        long duration_micro = (end_time - start_time) / 1000;
        System.out.println(String.format("Process complete, %d microseconds taken",duration_micro));
    }
}