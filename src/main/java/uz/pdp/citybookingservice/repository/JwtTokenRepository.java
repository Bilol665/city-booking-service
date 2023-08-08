package uz.pdp.citybookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citybookingservice.dto.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken,String > {

}
