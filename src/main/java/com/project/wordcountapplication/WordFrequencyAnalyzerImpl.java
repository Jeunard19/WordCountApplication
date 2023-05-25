package com.project.wordcountapplication;

import com.project.wordcountapplication.interfaces.WordFrequency;
import com.project.wordcountapplication.interfaces.WordFrequencyAnalyzer;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/wordfrequency")
public class WordFrequencyAnalyzerImpl implements WordFrequencyAnalyzer {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    @Override
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/highest-frequency")
    public int calculateHighestFrequency(@QueryParam("text") String text) {

        int frequency = Collections.max(this.getFrequencyMap(text).values()).intValue();

        return frequency;
    }

    @Override
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/frequency-word")
    public int calculateFrequencyForWord(@QueryParam("text") String text, @QueryParam("word") String word) {
        int  frequency = 0;
        try {
            frequency = this.getFrequencyMap(text).get(word.toLowerCase()).intValue();
        } catch(NullPointerException e){
        }

        return frequency;
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/most-frequent-nwords")
    public List<WordFrequency> calculateMostFrequentNWords( @QueryParam("text") String text, @QueryParam("n") int n) {

        List<WordFrequency> mostFrequentNWords = new ArrayList();
        Map<String, Long> frequencyMap = this.getFrequencyMap(text);
        List<String> mostFrequentWords = new ArrayList<>(frequencyMap.keySet());

        mostFrequentWords.sort(Comparator.comparingLong((String word) ->
                frequencyMap.get(word)).reversed().thenComparing(word -> word));


        mostFrequentWords.subList(0,n).forEach(word -> {
            mostFrequentNWords.add(new WordFrequencyImpl(word,frequencyMap.get(word).intValue()));
        });

        return mostFrequentNWords;
    }

    private Map<String,Long> getFrequencyMap(String text) {
        return new ArrayList<>(Arrays.asList(text.split("\\P{L}+"))).stream()
                .map(String::toLowerCase)
                .collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ));
    }

}