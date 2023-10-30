package dev.vimlesh.userservicetestfinal.repositories;

import dev.vimlesh.userservicetestfinal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
