package br.com.onebr.model;

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
@Table(name = "page_access", schema = "public")
public class PageAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPageAccess")
    @SequenceGenerator(name = "seqPageAccess", sequenceName = "seq_page_access", allocationSize = 1)
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "hits")
    private Long hits;
}
