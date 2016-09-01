package com.searchcode.app.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.searchcode.app.config.Values;
import com.searchcode.app.dto.OWASPMatchingResult;
import com.searchcode.app.model.OWASPResult;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OWASPClassifier {

    private String DATABASEPATH = Properties.getProperties().getProperty(Values.OWASPDATABASELOCATION, Values.DEFAULTOWASPDATABASELOCATION);
    private ArrayList<OWASPResult> database = new ArrayList<>();

    public OWASPClassifier() {
        this.database = this.loadDatabase();
    }

    public List<OWASPMatchingResult> classifyCode(List<String> codeLines) {
        ArrayList<OWASPMatchingResult> matching = new ArrayList<>();

        if (codeLines == null || codeLines.isEmpty()) {
            return matching;
        }

        for (OWASPResult result: this.database) {
            for (int i = 0; i < codeLines.size(); i++) {
                if (codeLines.get(i).toLowerCase().contains(result.name.toLowerCase())) {
                    matching.add(new OWASPMatchingResult(result.name, result.desc, result.type, i));
                }
            }
        }
        return matching;
    }

    public List<OWASPResult> getDatabase() {
        return this.database;
    }

    public void clearDatabase() {
        this.database.clear();
    }

    public void addToDatabase(OWASPResult result) {
        this.database.add(result);
    }

    private ArrayList<OWASPResult> loadDatabase() {
        ArrayList<OWASPResult> database = new ArrayList<>();

        try {
            Gson gson = new GsonBuilder().create();
            OWASPResult[] myArray = gson.fromJson(new FileReader(this.DATABASEPATH), OWASPResult[].class);
            database = new ArrayList<>(Arrays.asList(myArray));
        }
        catch (FileNotFoundException | JsonSyntaxException ex) {
            System.out.println(ex);
        }

        return database;
    }
}
