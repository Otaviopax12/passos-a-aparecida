package br.com.turismoreligioso.SantuarioAparecida.model;

public enum StatusHospedagem {
    PENDENTE("Pendente de Aprovação"),
    APROVADO("Aprovado"),
    REPROVADO("Reprovado");

    private final String displayValue;

    StatusHospedagem(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}