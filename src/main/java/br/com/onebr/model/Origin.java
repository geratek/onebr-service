package br.com.onebr.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "origin", schema = "public")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Origin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOrigin")
    @SequenceGenerator(name = "seqOrigin", sequenceName = "seq_origin", allocationSize = 1)
    private Long id;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_pt")
    private String namePt;
}
