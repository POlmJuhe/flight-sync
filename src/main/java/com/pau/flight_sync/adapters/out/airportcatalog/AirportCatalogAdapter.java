package com.pau.flight_sync.adapters.out.airportcatalog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pau.flight_sync.domain.port.AirportLookupPort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirportCatalogAdapter implements AirportLookupPort {

    private final ObjectMapper objectMapper;
    private Map<String, String> catalog;

    @PostConstruct
    void init() {
        try {
            val entries = objectMapper.readValue(
                    new ClassPathResource("airports.json").getInputStream(),
                    new TypeReference<List<AirportEntry>>() {}
            );
            catalog = entries.stream()
                    .filter(entry -> entry.icaoCode() != null && !entry.icaoCode().isBlank())
                    .collect(Collectors.toMap(
                            entry -> entry.icaoCode().toUpperCase(),
                            AirportEntry::name,
                            (first, second) -> first
                    ));
            log.info("Airport catalog loaded: {} entries", catalog.size());
        } catch (Exception e) {
            log.error("Failed to load airport catalog", e);
            catalog = Map.of();
        }
    }

    @Override
    public Optional<String> findName(String icaoCode) {
        if (icaoCode == null || icaoCode.isBlank()) return Optional.empty();
        return Optional.ofNullable(catalog.get(icaoCode.toUpperCase()));
    }

    @Override
    public Map<String, String> findAll() {
        return new TreeMap<>(catalog);
    }

    private record AirportEntry(String icaoCode, String name) {}
}
