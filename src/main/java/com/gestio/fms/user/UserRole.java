package com.gestio.fms.user;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

@Getter
public enum UserRole {
    TENANT(Sets.newHashSet(UserPermission.TICKET_READ, UserPermission.TICKET_WRITE)),
    STAFF(Sets.newHashSet(UserPermission.TICKET_READ, UserPermission.TICKET_WRITE, UserPermission.TENANT_READ)),
    ADMIN(Sets.newHashSet(UserPermission.TICKET_READ, UserPermission.TICKET_WRITE, UserPermission.TENANT_READ, UserPermission.TENANT_WRITE, UserPermission.STAFF_READ, UserPermission.STAFF_WRITE));

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    private final Set<UserPermission> permissions;
}