package com.alexandreloiola.MenuRbac.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="TB_MENU_ITEM")
public class MenuItemModel {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition="uuid")
    private UUID id;

    @Column(name = "title", nullable = false, unique = true, length = 100)
    private String title;

    @Column(name = "uri", nullable = false, unique = true, length = 200)
    private String uri;

    @Column(name = "icon_uri", nullable = true, unique = true, length = 200)
    private String iconUri;

    @OneToOne
    @JoinColumn(name="next_menu_item_id")
    private MenuItemModel nextMenuItem;

    @ManyToOne
    @JoinColumn(name="parent_menu_item_id")
    private MenuItemModel parentMenuItemModelId;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "TB_ROLE_MENU_ITEM",
            joinColumns = {@JoinColumn(name = "menu_item_id", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleModel> roles = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItemModel)) return false;
        MenuItemModel that = (MenuItemModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
