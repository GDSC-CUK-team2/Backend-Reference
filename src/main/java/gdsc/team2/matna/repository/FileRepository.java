package gdsc.team2.matna.repository;

import gdsc.team2.matna.entity.FileEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository  extends JpaRepository<FileEntity,Long> {


}
