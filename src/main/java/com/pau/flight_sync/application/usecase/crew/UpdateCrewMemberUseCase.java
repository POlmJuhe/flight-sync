package com.pau.flight_sync.application.usecase.crew;

import com.pau.flight_sync.application.dto.AddCrewMemberForm;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCrewMemberUseCase {

    private final FlightRepositoryPort flightRepository;

    @Transactional
    public void execute(UUID flightId, UUID crewMemberId, AddCrewMemberForm form) {
        flightRepository.updateCrewMember(
                flightId, crewMemberId,
                form.getFirstName().trim(),
                form.getLastName().trim(),
                form.getFunction().trim().toUpperCase()
        );
    }
}
