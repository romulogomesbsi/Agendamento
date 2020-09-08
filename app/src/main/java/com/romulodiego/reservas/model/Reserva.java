package com.romulodiego.reservas.model;

import java.util.Date;

public class Reserva {

    private String ambienteID;
    private String documentID;
    private Date data_inicio;
    private Date data_fim;
    private String hora_inicio;
    private String duracao;
    private Date data_criacao;
    private String nome_ambiente;

    public Reserva() {
    }

    public String getAmbienteID() {
        return ambienteID;
    }

    public void setAmbienteID(String ambienteID) {
        this.ambienteID = ambienteID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public String getNome_ambiente() {
        return nome_ambiente;
    }

    public void setNome_ambiente(String nome_ambiente) {
        this.nome_ambiente = nome_ambiente;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public Date getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    public Date getData_fim() {
        return data_fim;
    }

    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public Date getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    @Override
    public String toString() {
        return  "Ambiente: "+ nome_ambiente + '\n' +
                "Data de Criação: " + data_criacao + '\n' +
                "Data Agendada: " + data_inicio + '\n' +
                "Duração da reserva: " + duracao + " horas" + '\n';
    }
}
