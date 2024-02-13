package de.mika.serverapi.user.rank;

import lombok.Getter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Getter
public enum Rank {

    USER(null), DEVELOPER(USER, Permission.BAN_SET, Permission.BAN_REMOVE), ADMIN (null, Permission.SUPER_ALL);

    private final Pattern TO_STRING_PATTERN = Pattern.compile(
            "Rank\\{permissions=\\[(.*)\\]\\}");

    private final Permission[] permissions;

      Rank(Rank copy, Permission... additionalPermissions) {
        if (copy != null) {
            this.permissions = Stream.concat(
                    Arrays.stream(copy.permissions),
                    Arrays.stream(additionalPermissions)
            ).toArray(Permission[]::new);
        } else {
            this.permissions = additionalPermissions;
        }
    }

    Rank(String toStringValue) {
          if(toStringValue == null) {
              this.permissions = new Permission[0];
              return;
          }
        Matcher matcher = TO_STRING_PATTERN.matcher(toStringValue);
        if (matcher.matches()) {
            String permissionsString = matcher.group(1);
            this.permissions = Arrays.stream(permissionsString.split(", "))
                    .map(Permission::valueOf)
                    .toArray(Permission[]::new);
        } else {
            throw new IllegalArgumentException("Invalid toString value: " + toStringValue);
        }
    }

    @Override
    public String toString() {
        return "Rank{" +
                "permissions=" + Arrays.toString(permissions) +
                '}';
    }

    public boolean hasPermission(Permission permission) {
        return Arrays.asList(permissions).contains(permission);
    }
}