package com.interset.interview;


import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.*;

import com.google.gson.*;

/*
I was able to start working on this assignment only from the later evening of Mar 25, 2018.

Computing the Average of a List of Values (Avg. number of Siblings) is O(n)
The most efficient algorithm for getting the top 3 favourite foods would be to use Trie + Heap.
However, that is only marginally better than the approach I have used.
The last question was also similar. However, the entire process together is close to O(n).

I mostly use Python and I was not having time to research on the frameworks for the best java implementation.
Similarly, I don't have much experience with JUnit, so I tested my code from my IDE (IntelliJ CE)
by passing the sample files as arguments in Run configuration.

*/


public class Runner {

    /**
     * This is main method which is starting point for this application.
     * It requires 1 arguments to run successfully.
     *
     * @param: args[0] : Path to JSON or CSV file to read.
     *
     * The JSON and CSV files must contain the following fields:
     *  name, siblings, favourite_food, birth_timezone, birth_timestamp
     *
     * This application parses the files and prints out the following information:
     *       - Average number of siblings (round up)
     *       - Top 3 favourite foods
     *       - How many people were born in each month of the year (uses the month of each person's respective timezone of birth)
     *
     */
    public static void main(String args[]) {

        if (args.length != 1) {
            System.out.println("We currently only expect 1 argument! A path to a JSON or CSV file to read.");
            System.exit(1);
        }

        System.out.println("Do cool stuff here!!");
        String file_name = args[0];
        System.out.println(file_name);
        String [] file_parts = file_name.split("\\.");
        System.out.println(file_parts.length);
        System.out.println(file_parts[0]);
        System.out.println(file_parts[1]);
        if (file_parts.length != 2){
            System.out.println("Unrecognized File Format. Could accept only a CSV or JSON File");
            System.exit(2);
        }
        if(file_parts[1].equals("csv")){
            System.out.println("Received a file with csv extension. Processing as a csv File");
            try {
                BufferedReader br = new BufferedReader(new FileReader(file_name));
                boolean eof       = false;
                boolean hdr       = true;
                float totalNumberOfSiblings = 0;
                float numberOfRecords = 0;
                Map<String, Integer> wordFrequencies = new HashMap<String, Integer>();
                List<String> mostFrequentWords       = new ArrayList<String>();
                int[] monthsOccurrence = new int[12];
                String [] months       = {"January", "February", "March", "April", "May", "June", "July", "August", "September","October", "November", "December"};

                while(!eof){
                    try{
                        String line = br.readLine();
                        if(line != null && !hdr){
                            String[] field_values = line.split(",");
                            totalNumberOfSiblings += Integer.valueOf(field_values[2]);
                            numberOfRecords++;

                            String word = field_values[3];
                            Integer wordCount = wordFrequencies.get(word);
                            wordFrequencies.put(word, wordCount=(wordCount==null)?1:wordCount+1);

                            if (mostFrequentWords.size() < 3){
                                if(!mostFrequentWords.contains(word))
                                    mostFrequentWords.add(word);
                            }else{
                                for(int i = 0; i < mostFrequentWords.size(); i++) {
                                    if(wordFrequencies.get(word) > wordFrequencies.get(mostFrequentWords.get(i))){
                                        mostFrequentWords.remove(i);
                                        mostFrequentWords.add(i, word);
                                    }
                                }
                            }
                            Long timeinMillis = Long.valueOf(field_values[5]);
                            Calendar cal     = Calendar.getInstance();
                            cal.setTimeInMillis(timeinMillis);
                            int idx     = cal.get(Calendar.MONTH);
                            monthsOccurrence[idx] += 1;
                        }
                        if(line == null)
                            eof = true;
                        if(hdr)
                            hdr = false;

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("Average Number of Siblings:" + String.valueOf(totalNumberOfSiblings/numberOfRecords)+"\n");
                System.out.println("Three Favourite Foods:");
                for(int i = 0; i < mostFrequentWords.size(); i++)
                    if (i < mostFrequentWords.size()-1)
                        System.out.println(String.valueOf(mostFrequentWords.get(i))+"("+wordFrequencies.get(String.valueOf(mostFrequentWords.get(i)))+"),");
                    else
                        System.out.println(String.valueOf(mostFrequentWords.get(i))+"("+wordFrequencies.get(String.valueOf(mostFrequentWords.get(i)))+")");
                System.out.println("Birth Months:");
                for(int i =0; i < 12; i++){
                    if (i < 11)
                        System.out.println(months[i]+"("+String.valueOf(monthsOccurrence[i])+") ,");
                    else
                        System.out.println(months[i]+"("+String.valueOf(monthsOccurrence[i])+")");
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        if(file_parts[1].equals("json")){
            System.out.println("Received a file with json extension. Processing as a JSON File");
            Gson gson = new Gson();
            try{
                Reader reader = new FileReader(file_name);
                JsonArray jsonArray = (JsonArray) new JsonParser().parse(reader);
                float numberOfRecords = 0;
                float totalNumberOfSiblings = 0;

                Map<String, Integer> wordFrequencies = new HashMap<String, Integer>();
                List<String> mostFrequentWords       = new ArrayList<String>();
                int[] monthsOccurrence = new int[12];
                String [] months       = {"January", "February", "March", "April", "May", "June", "July", "August", "September","October", "November", "December"};

                for(Iterator iterator = jsonArray.iterator(); iterator.hasNext();){
                    JsonObject jo = (JsonObject) iterator.next();
                    totalNumberOfSiblings += jo.get("siblings").getAsInt();
                    numberOfRecords       += 1;

                    String word       = jo.get("favourite_food").toString();
                    Integer wordCount = wordFrequencies.get(word);
                    wordFrequencies.put(word, wordCount=(wordCount==null)?1:wordCount+1);

                    if (mostFrequentWords.size() < 3){
                        if(!mostFrequentWords.contains(word))
                            mostFrequentWords.add(word);
                    }else{
                        for(int i = 0; i < mostFrequentWords.size(); i++) {
                            if(wordFrequencies.get(word) > wordFrequencies.get(mostFrequentWords.get(i))){
                                mostFrequentWords.remove(i);
                                mostFrequentWords.add(i, word);
                            }
                        }
                    }
                    Long timeinMillis = Long.valueOf(jo.get("birth_timestamp").getAsLong());
                    Calendar cal     = Calendar.getInstance();
                    cal.setTimeInMillis(timeinMillis);
                    int idx     = cal.get(Calendar.MONTH);
                    monthsOccurrence[idx] += 1;
                }
                System.out.println("Average Number of Siblings:" + String.valueOf(totalNumberOfSiblings/numberOfRecords)+"\n");
                System.out.println("Three Favourite Foods:");
                for(int i = 0; i < mostFrequentWords.size(); i++)
                    if (i < mostFrequentWords.size()-1)
                        System.out.println(String.valueOf(mostFrequentWords.get(i))+"("+wordFrequencies.get(String.valueOf(mostFrequentWords.get(i)))+"),");
                    else
                        System.out.println(String.valueOf(mostFrequentWords.get(i))+"("+wordFrequencies.get(String.valueOf(mostFrequentWords.get(i)))+")");
                System.out.println("Birth Months:");
                for(int i =0; i < 12; i++){
                    if (i < 11)
                        System.out.println(months[i]+"("+String.valueOf(monthsOccurrence[i])+") ,");
                    else
                        System.out.println(months[i]+"("+String.valueOf(monthsOccurrence[i])+")");
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

    }
}
