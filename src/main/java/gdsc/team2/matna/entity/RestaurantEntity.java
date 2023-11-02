package gdsc.team2.matna.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="restaurant")
public class RestaurantEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long restaurantId;

    @Column
    @NotNull
    private String restaurantName;


}
