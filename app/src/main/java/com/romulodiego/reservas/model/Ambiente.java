package com.romulodiego.reservas.model;

import java.util.List;

public class Ambiente {
    private String documentId;
    private String nome;

    public Ambiente() {
    }
    public Ambiente(String nome, String documentId) {
        this.documentId = documentId;
        this.nome = nome;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}

