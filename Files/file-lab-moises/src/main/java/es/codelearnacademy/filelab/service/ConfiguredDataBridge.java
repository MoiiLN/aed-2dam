package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.config.PropertiesConfig;

import java.nio.file.Path;

public class ConfiguredDataBridge {

    private final PropertiesConfig propertiesConfig;
    private final DataBridgeService dataBridgeService;

    public ConfiguredDataBridge(PropertiesConfig propertiesConfig,
                                DataBridgeService dataBridgeService) {
        this.propertiesConfig = propertiesConfig;
        this.dataBridgeService = dataBridgeService;
    }

    public int execute() {
        String inputFormat = propertiesConfig.getOrDefault("input.format", "");
        String inputFile = propertiesConfig.getOrDefault("input.file", "");
        String outputFormat = propertiesConfig.getOrDefault("output.format", "");
        String outputFile = propertiesConfig.getOrDefault("output.file", "");
        if (inputFormat.isBlank() ||
                inputFile.isBlank() ||
                outputFormat.isBlank() ||
                outputFile.isBlank()) {
            return 0;
        }
        try {
            FileFormat origenFormato = FileFormat.from(inputFormat);
            Path origen = Path.of(inputFile);

            FileFormat destinoFormato = FileFormat.from(outputFormat);
            Path destino = Path.of(outputFile);

            return dataBridgeService.convert(
                    origenFormato,
                    origen,
                    destinoFormato,
                    destino
            );
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
}