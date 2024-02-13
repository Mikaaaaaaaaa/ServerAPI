package de.mika.serverapi.user;

import de.mika.serverapi.database.ServerDatabase;
import de.mika.serverapi.user.ban.Ban;
import de.mika.serverapi.user.rank.Rank;
import lombok.Getter;

@Getter
public class ServerUser
{

    private final String uuid;
    private final Rank rank;
    private final Ban ban;

    public ServerUser(String uuid)
    {
        this.uuid = uuid;
        this.rank = ServerDatabase.getInstance().loadPlayer(Rank.class, uuid);
        this.ban = ServerDatabase.getInstance().loadPlayer(Ban.class, uuid);
    }

}
