package alex9932.engine;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamFriends;
import com.codedisaster.steamworks.SteamFriends.FriendFlags;
import com.codedisaster.steamworks.SteamFriends.PersonaChange;
import com.codedisaster.steamworks.SteamFriendsCallback;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

public class Test {
	public static void main(String[] args) throws SteamException {
		SteamAPI.init();
		SteamAPI.printDebugInfo(System.out);
		if(!SteamAPI.isSteamRunning(true)){
			TinyFileDialogs.tinyfd_messageBox("Error!", "Vape not running!", "error", "error", true);
			System.exit(-1);
		}
		
		SteamFriends friends = new SteamFriends(new SteamFriendsCallback() {
			@Override
			public void onSetPersonaNameResponse(boolean arg0, boolean arg1, SteamResult arg2) {
				
			}
			
			@Override
			public void onPersonaStateChange(SteamID arg0, PersonaChange arg1) {
				
			}
			
			@Override
			public void onGameServerChangeRequested(String arg0, String arg1) {
				
			}
			
			@Override
			public void onGameRichPresenceJoinRequested(SteamID arg0, String arg1) {
				
			}
			
			@Override
			public void onGameOverlayActivated(boolean arg0) {
				
			}
			
			@Override
			public void onGameLobbyJoinRequested(SteamID arg0, SteamID arg1) {
				
			}
			
			@Override
			public void onFriendRichPresenceUpdate(SteamID arg0, int arg1) {
				
			}
			
			@Override
			public void onAvatarImageLoaded(SteamID arg0, int arg1, int arg2, int arg3) {
				
			}
		});
		
		for (int i = 0; i < friends.getFriendCount(FriendFlags.All); i++) {
			SteamID friend = friends.getFriendByIndex(0, FriendFlags.All);
			System.out.println(friend.getAccountID());
		}

	}
}