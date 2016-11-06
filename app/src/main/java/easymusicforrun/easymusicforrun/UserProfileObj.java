package easymusicforrun.easymusicforrun;

/**
 * Created by samrudhi on 11/6/16.
 */
public class UserProfileObj {

    String localPlayListName = "Usher";
    String youtubePlaylistName = "User";
    int minWalkingSpeed = 10;
    int minRunningSpeed = 20;

    public String getLocalPlayListName() {
        return localPlayListName;
    }

    public void setLocalPlayListName(String localPlayListName) {
        this.localPlayListName = localPlayListName;
    }


    public String getYoutubePlaylistName() {
        return youtubePlaylistName;
    }

    public void getYoutubePlaylistName(String youtubePlaylistName) {
        this.youtubePlaylistName = youtubePlaylistName;
    }

    public int getMinWalkingSpeed() {
        return minWalkingSpeed;
    }

    public void setMinWalkingSpeed(int minWalkingSpeed){
        this.minWalkingSpeed = minWalkingSpeed;
    }

    public int getMinRunningSpeed() {
        return minRunningSpeed;
    }

    public void setMinRunningSpeed(int minRunningSpeed) {
        this.minRunningSpeed = minRunningSpeed;
    }
}
