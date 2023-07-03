package com.gestio.fms.auth.entities;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

import static com.gestio.fms.auth.entities.UserPermission.*;

@Getter
public enum UserRole {
    TENANT(Sets.newHashSet(TICKET_READ,TICKET_WRITE)),
    STAFF(Sets.newHashSet(TICKET_READ,TICKET_WRITE,TENANT_READ)),
    ADMIN(Sets.newHashSet(TICKET_READ,TICKET_WRITE,TENANT_READ,TENANT_WRITE,STAFF_READ,STAFF_WRITE));

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    private final Set<UserPermission> permissions;
}