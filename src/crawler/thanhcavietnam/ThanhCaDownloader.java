package crawler.thanhcavietnam;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ThanhCaDownloader {

	public static void main(String[] args) {
		downloadType("Bo Le");
	}
	public static void downloadType(String type){
		ThanhCaDataManager.getInstance().parse("thanhca/The Loai/"+Utils.getStandardFileName(type)+".xml");
		Vector<ThanhCaSongInfo> album = ThanhCaDataManager.getInstance().getSongs();
		for (int i = 0;i<album.size();i++)
			downloadSong(album.elementAt(i));
	}
	public static void downloadAlbum(int albumId){
		Vector<ThanhCaSongInfo> album = ThanhCaDataManager.getInstance().getAlbum(225);//"Cau Cho Cha Me"
		for (int i = 0;i<album.size();i++)
			downloadSong(album.elementAt(i));
	}
	
	public static void downloadSong(ThanhCaSongInfo song){
		getLogger().info("Downloading[song=" + song.getName() +"; album=" + song.getAlbumName()+"]");
		downloadFile(song.getAudio(), "/Users/BachPham/Downloads/ThanhCa/" + Utils.getStandardFileName(song.getAlbumName()));
	}
	
	public static void downloadFile(String url, String path) {
		try {
			Downloader mDownloader = new Downloader(new URL(url), new File(path));
			new Thread(mDownloader).start();
			int lastPercent = mDownloader.getProgressPercent();
			mDownloader.isProgressUpdated();
//			System.out.println(url + " is downloading; destination " + path);
			while (!mDownloader.isCompleted()) {
				if (mDownloader.isProgressUpdated()) {
					if (mDownloader.getProgressPercent() > lastPercent) {
						lastPercent = mDownloader.getProgressPercent();
						System.out.print(" " + lastPercent);
					}
				}
				try {
					Thread.sleep(5);
				} catch (Exception e) {
				}
			}
			getLogger().info(path.concat("/") + mDownloader.getFile() + "\t" + mDownloader.getProgressString());
			try {
				mDownloader.waitUntilCompleted();
			} catch (Exception e) {
				getLogger().error(url+"\t" +path +"\t" + e);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private static Logger getLogger() {
		// TODO Auto-generated method stub
		return Utils.getLogger();
	}
}
