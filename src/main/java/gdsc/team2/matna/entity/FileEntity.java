package gdsc.team2.matna.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.repository.Lock;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="file_mgt_tb")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_logic_id")
    @NotNull
    private String fileLogicId;

    @Column(name = "file_org_nm")
    @NotNull
    private String fileOrgName;



}



