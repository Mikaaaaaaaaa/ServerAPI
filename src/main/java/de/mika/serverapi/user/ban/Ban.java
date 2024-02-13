package de.mika.serverapi.user.ban;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class Ban
{
    private static final Pattern TO_STRING_PATTERN = Pattern.compile(
            "Ban\\{banTime=(\\d+), muteTime=(\\d+), banReason='(.*)', muteReason='(.*)'\\}");

    private long banTime,
            muteTime;

    private String banReason,
            muteReason;

    public Ban() {
        this(0, 0, "", "");
    }

    public Ban(long banTime, long muteTime, String banReason, String muteReason) {
        this.banTime = banTime;
        this.muteTime = muteTime;
        this.banReason = banReason;
        this.muteReason = muteReason;
    }

    public Ban(String toStringValue) {
        Matcher matcher = TO_STRING_PATTERN.matcher(toStringValue);
        if (matcher.matches()) {
            this.banTime = Long.parseLong(matcher.group(1));
            this.muteTime = Long.parseLong(matcher.group(2));
            this.banReason = matcher.group(3);
            this.muteReason = matcher.group(4);
        } else {
            throw new IllegalArgumentException("Invalid toString value: " + toStringValue);
        }
    }

    @Override
    public String toString() {
        return "Ban{" +
                "banTime=" + banTime +
                ", muteTime=" + muteTime +
                ", banReason='" + banReason + '\'' +
                ", muteReason='" + muteReason + '\'' +
                '}';
    }
}