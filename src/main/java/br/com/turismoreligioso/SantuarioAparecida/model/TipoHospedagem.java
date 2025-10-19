package br.com.turismoreligioso.SantuarioAparecida.model;

public enum TipoHospedagem {
    // Adiciona um valor amigável para exibição
    HOTEL("Hotel"),
    POUSADA("Pousada"),
    CASA_DE_RETIRO("Casa de Retiro"),
    ALBERGUE("Albergue");

    private final String displayValue;

    TipoHospedagem(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

