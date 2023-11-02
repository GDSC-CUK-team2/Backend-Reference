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
    @Column
    private Long fileId;


    @NotNull
    @Column
    private String fileLogicId;


    @NotNull
    @Column
    private String fileOrgName;



}



