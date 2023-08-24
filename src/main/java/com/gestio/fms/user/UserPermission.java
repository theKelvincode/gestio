package com.gestio.fms.user;

import lombok.Getter;

@Getter
public enum UserPermission {
    TICKET_READ("ticket:read"),
    TICKET_WRITE("ticket:write"),
    TENANT_READ("tenant:read"),
    TENANT_WRITE("tenant:write"),
    STAFF_READ("staff:read"),
    STAFF_WRITE("staff:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }
}
