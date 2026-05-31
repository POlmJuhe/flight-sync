package com.pau.flight_sync.application.usecase.crew;

import com.pau.flight_sync.application.dto.AddCrewMemberForm;
import com.pau.flight_sync.domain.model.CrewMember;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddCrewMemberUseCase {

    private final FlightRepositoryPort flightRepository;

    @Transactional
    public void execute(UUID flightId, AddCrewMemberForm form) {
        val member = new CrewMember(
                null, null,
                form.getFirstName().trim(),
                form.getLastName().trim(),
                form.getFunction().trim().toUpperCase()
        );
        flightRepository.addCrewMember(flightId, member);
    }
}
