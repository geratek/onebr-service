package br.com.onebr.model.resistome;

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
@Table(name = "phenicol", schema = "public")
public class Phenicol implements ResistomeBaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPhenicol")
    @SequenceGenerator(name = "seqPhenicol", sequenceName = "seq_resistome_att", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;
}
