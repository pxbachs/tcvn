package crawler.thanhcavietnam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import utils.io.xml.Element;
import utils.io.xml.LightXmlParser;

public class ThanhCaDataManager {
	private static ThanhCaDataManager instance = new ThanhCaDataManager();
	private Vector<ThanhCaSongInfo> listSong = new Vector<ThanhCaSongInfo>();

	private ThanhCaDataManager() {

	}

	public void parse(String xmlFile) {
		try {
			listSong = new Vector<ThanhCaSongInfo>();
			Element songs = LightXmlParser.parse(new FileInputStream(xmlFile), "UTF-8").getElement("songs");
			for (int i = 0; i < songs.getChildCount(); i++) {
				Element song = songs.getElement(i);
				int albumId = Integer.parseInt(song.getElement("albumid").getContent());
				int songId = Integer.parseInt(song.getElement("songid").getContent());
				String name = song.getElement("name").getContent();
				String artist = song.getElement("artist").getContent();
				String author = song.getElement("author").getContent();
				String albumName = song.getElement("album").getContent();
				String type = song.getElement("type").getContent();
				String audio = song.getElement("audio").getContent();
				String pdf = song.getElement("pdf").getContent();

				ThanhCaSongInfo songinfo = new ThanhCaSongInfo(albumId, songId, name, artist, author, albumName, type, audio, pdf);
				listSong.add(songinfo);
				// System.out.println(songinfo);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Vector<ThanhCaSongInfo> getAlbum(int albumId) {
		Vector<ThanhCaSongInfo> songs = new Vector<ThanhCaSongInfo>();

		for (int i = 0; i < listSong.size(); i++)
			if (listSong.get(i).getAlbumId() == albumId)
				songs.add(listSong.elementAt(i));

		return songs;
	}

	public Vector<ThanhCaSongInfo> getSongs() {
		return listSong;
	}

	public Vector<ThanhCaSongInfo> getAlbum(String albumName) {
		Vector<ThanhCaSongInfo> songs = new Vector<ThanhCaSongInfo>();

		for (int i = 0; i < listSong.size(); i++)
			if (albumName.equalsIgnoreCase(Utils.getStandardFileName(listSong.get(i).getAlbumName())))
				songs.add(listSong.elementAt(i));

		return songs;
	}

	public static void saveToXML(String filepath, Vector<ThanhCaSongInfo> songs) throws Exception{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filepath)), "UTF-8"));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<songs>");
		for (int i = 0; i < songs.size(); i++)
			writer.write(songs.elementAt(i).toXML());
		writer.write("\n</songs>");
		writer.close();
	}
	
	public static ThanhCaDataManager getInstance() {
		return instance;
	}
}
