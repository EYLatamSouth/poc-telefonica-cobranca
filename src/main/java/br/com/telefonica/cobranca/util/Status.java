package br.com.telefonica.cobranca.util;

public enum Status {

    Processed("0"),
    NotProcessed("1"),
    Error("2");



    public final String label;

    private Status(String label) {
        this.label = label;
    }
}
