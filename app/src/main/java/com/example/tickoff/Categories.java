package com.example.tickoff;

public enum Categories {
    TANULAS("Tanulás"),
    MUNKA("Munka"),
    BEVASARLAS("Bevásárlás"),
    CSALAD("Család"),
    SZAMLAK("Számlák");

    private String category;
    Categories(String cat){
        this.category = cat;
    }

    public static String getCategory(int index){
        index--;
        String category = "";
        switch (Categories.values()[index]){
            case TANULAS:
                category = "Tanulás";
                break;
            case MUNKA:
                category = "Munka";
                break;
            case BEVASARLAS:
                category = "Bevásárlás";
                break;
            case CSALAD:
                category = "Család";
                break;
            case SZAMLAK:
                category = "Számlák";
                break;
        }
        return category;
    }

    public static int getIndex(String adat){
        int index = -1;
        switch (adat){
            case "Tanulás":
                index = Categories.valueOf("TANULAS").ordinal();
                break;
            case "Munka":
                index = Categories.valueOf("MUNKA").ordinal();
                break;
            case "Bevásárlás":
                index = Categories.valueOf("BEVASARLAS").ordinal();
                break;
            case "Család":
                index = Categories.valueOf("CSALAD").ordinal();
                break;
            case "Számlák":
                index = Categories.valueOf("SZAMLAK").ordinal();
                break;
        }
        return ++index;
    }
}
