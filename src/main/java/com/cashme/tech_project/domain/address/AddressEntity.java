package com.cashme.tech_project.domain.address;

import com.cashme.tech_project.domain.client.ClientEntity;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @Column(name = "address")
    private String address;

    @Column(name = "number")
    private String number;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "uf")
    private String uf;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private ClientEntity client;
}
