package com.cashme.tech_project.domain.client;

import com.cashme.tech_project.domain.address.AddressEntity;
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
@Table(name = "client")
public class ClientEntity {

    @Id
    @With
    @GeneratedValue
    @Column(name = "client_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "national_identity_type")
    private NationalIdentityType nationalIdentityType;

    @Column(name = "national_identity")
    private String nationalIdentity;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private AddressEntity address;

    public void updateAddress(final AddressEntity newAddress) {
        if (this.address == null) {
            this.address = new AddressEntity();
        }

        this.address.setAddressType(newAddress.getAddressType());
        this.address.setAddress(newAddress.getAddress());
        this.address.setNumber(newAddress.getNumber());
        this.address.setNeighborhood(newAddress.getNeighborhood());
        this.address.setZipCode(newAddress.getZipCode());
        this.address.setCity(newAddress.getCity());
        this.address.setUf(newAddress.getUf());
    }
}
