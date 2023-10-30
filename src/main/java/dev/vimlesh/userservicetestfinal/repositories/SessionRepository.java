package dev.vimlesh.userservicetestfinal.repositories;

import dev.vimlesh.userservicetestfinal.models.Session;
import dev.vimlesh.userservicetestfinal.models.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
}
