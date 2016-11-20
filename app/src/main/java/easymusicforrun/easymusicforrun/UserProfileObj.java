package easymusicforrun.easymusicforrun;

public class UserProfileObj {

    String localPlayListName = "Usher";
    String youtubePlaylistName = "User";
    double minWalkingSpeed = -0.01;
    double minRunningSpeed = -0.01;

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

    public double getMinWalkingSpeed() {
        return minWalkingSpeed;
    }

    public void setMinWalkingSpeed(int minWalkingSpeed){
        this.minWalkingSpeed = minWalkingSpeed;
    }

    public double getMinRunningSpeed() {
        return minRunningSpeed;
    }

    public void setMinRunningSpeed(int minRunningSpeed) {
        this.minRunningSpeed = minRunningSpeed;
    }
}
