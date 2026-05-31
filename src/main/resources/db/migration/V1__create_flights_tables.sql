CREATE TABLE flight (
    id               UUID        PRIMARY KEY,
    external_id      BIGINT      NOT NULL,
    registration     VARCHAR(10) NOT NULL,
    departure_airport VARCHAR(4)    NOT NULL,
    arrival_airport   VARCHAR(4)    NOT NULL,
    departure_time   TIMESTAMP   NOT NULL,
    landing_time     TIMESTAMP   NOT NULL,
    distance_km      INTEGER     NOT NULL,
    flight_type      VARCHAR(20) NOT NULL,
    CONSTRAINT uq_flight_external_id UNIQUE (external_id)
);

CREATE TABLE crew_member (
    id           UUID         PRIMARY KEY,
    flight_id    UUID         NOT NULL,
    id_user      BIGINT       NOT NULL,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    func         VARCHAR(10)  NOT NULL,
    CONSTRAINT fk_crew_flight FOREIGN KEY (flight_id) REFERENCES flight(id) ON DELETE CASCADE
);

CREATE INDEX idx_flight_departure_time ON flight (departure_time DESC);
CREATE INDEX idx_crew_member_flight_id ON crew_member (flight_id);
