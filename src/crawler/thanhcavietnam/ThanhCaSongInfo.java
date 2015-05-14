package crawler.thanhcavietnam;

public class ThanhCaSongInfo {
	private int albumId;
	private int songId;
	private String name;
	private String artist;
	private String author;
	private String albumName;
	private String type;
	private String audio;
	private String pdf;

	public ThanhCaSongInfo(int albumId, int songId, String name, String artist, String author, String albumName, String type, String audio, String pdf) {
		this.albumId = albumId;
		this.songId = songId;
		this.name = name;
		this.artist = artist;
		this.author = author;
		this.albumName = albumName;
		this.type = type;
		this.audio = audio;
		this.pdf = pdf;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public void update(int albumId, int songId, String name, String artist, String author, String albumName, String type, String audio, String pdf) {
		this.albumId = albumId;
		this.songId = songId;
		this.name = name;
		this.artist = artist;
		this.author = author;
		this.albumName = albumName;
		this.type = type;
		this.audio = audio;
		this.pdf = pdf;
	}

	public String toString() {
		return "" + albumId + "\t" + songId + "\t" + name + "\t" + artist + "\t" + author + "\t" + albumName + "\t" + type + "\t" + audio + "\t" + pdf;
	}

	public String toXML() {
		return String.format("\n\t<song>\n\t\t<albumid>%s</albumid>\n\t\t<songid>%s</songid>\n\t\t<name>%s</name>\n\t\t<artist>%s</artist>\n\t\t<author>%s</author>\n\t\t<album>%s</album>\n\t\t<type>%s</type>\n\t\t<audio>%s</audio>\n\t\t<pdf>%s</pdf>\n\t</song>", albumId, songId, name, artist, author, albumName, type, audio, pdf);
	}

	public String getTextDetail() {
		// TODO Auto-generated method stub
		return this.toString();
	}
}
