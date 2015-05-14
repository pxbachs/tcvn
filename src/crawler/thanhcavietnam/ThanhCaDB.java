package crawler.thanhcavietnam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThanhCaDB {
	public static final String OUTPUT_FOLDER = "";
	public static final String URL_MAIN = "http://thanhcavietnam.net/ThanhCaVN/index.php";
	public static final String URL_DETAIL = "http://thanhcavietnam.net/ThanhCaVN/index.php";

	public static void main(String[] args) {
//		getAllAlbum();
//		makeDB(true);
		testexport();
	}

	public static void testexport(){
		try {
			Response mRes = Jsoup.connect(URL_MAIN).execute();
			Map<String, String> mCookies = mRes.cookies();
			
			ThanhCaDataManager.getInstance().parse("thanhca/thanhcadb.xml");
			Vector<ThanhCaSongInfo> album = ThanhCaDataManager.getInstance().getSongs();
			int index = 0;
			
			for (int i = 0;i<album.size();i++){
				ThanhCaSongInfo song = album.get(i);
				if(!song.getAudio().endsWith(".mp3") && !song.getAudio().endsWith(".wmv") ){
					System.out.println((index++) + "\t" + song);
					String songdetail = song.getAlbumId() + "\t" + getSongDetail(URL_DETAIL, mCookies, "" + song.getSongId());
					String[] token = songdetail.split("\t");
					if (token.length < 6) {

					} else {
						int token_index = 0;
						song.update(Integer.parseInt(token[token_index++]), Integer.parseInt(token[token_index++]), StringEscapeUtils.escapeXml(token[token_index++]), StringEscapeUtils.escapeXml(token[token_index++]), StringEscapeUtils.escapeXml(token[token_index++]), StringEscapeUtils.escapeXml(token[token_index++]), StringEscapeUtils.escapeXml(token[token_index++]), StringEscapeUtils.escapeXml(token[token_index++]), (token.length < 9 ? "" : StringEscapeUtils.escapeXml(token[token_index++])));
						System.out.println(index + "\t" + song);
					}
				}
			}
				
			ThanhCaDataManager.saveToXML("thanhca/tcdb.xml", album);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void makeDB(boolean byType) {
		if (byType) {
			Vector<String> types = listType();
			for (int ii = 0; ii < types.size(); ii++) {
				String type = types.elementAt(ii);
				try {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("thanhca/The Loai/" + Utils.getStandardFileName(type) + ".xml")), "UTF-8"));
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("thanhca/thanhcavietnam.txt")), "UTF-8"));
					String line;
					int index = 0;
					writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<songs>\n");
					String xml = "\t<song>\n\t\t<albumid>%s</albumid>\n\t\t<songid>%s</songid>\n\t\t<name>%s</name>\n\t\t<artist>%s</artist>\n\t\t<author>%s</author>\n\t\t<album>%s</album>\n\t\t<type>%s</type>\n\t\t<audio>%s</audio>\n\t\t<pdf>%s</pdf>\n\t</song>\n";
					while ((line = reader.readLine()) != null) {
						String[] token = line.split("\t");
						index++;
						if (token.length < 6) {

						} else {
							if (!type.equals(token[6]))
								continue;
							int i = 0;
							String tmp = String.format(xml, token[i++], token[i++], StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), (token.length < 9 ? "" : StringEscapeUtils.escapeXml(token[i++])));
							System.out.println(index + "\t" + tmp);
							writer.write(tmp);
						}
					}
					writer.write("</songs>");
					writer.close();
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("thanhca/thanhcadb.xml")), "UTF-8"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("thanhca/thanhcavietnam.txt")), "UTF-8"));
				String line;
				int index = 0;
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<songs>\n");
				String xml = "\t<song>\n\t\t<albumid>%s</albumid>\n\t\t<songid>%s</songid>\n\t\t<name>%s</name>\n\t\t<artist>%s</artist>\n\t\t<author>%s</author>\n\t\t<album>%s</album>\n\t\t<type>%s</type>\n\t\t<audio>%s</audio>\n\t\t<pdf>%s</pdf>\n\t</song>\n";
				while ((line = reader.readLine()) != null) {
					String[] token = line.split("\t");
					index++;
					if (token.length < 6) {

					} else {
						int i = 0;
						String tmp = String.format(xml, token[i++], token[i++], StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), StringEscapeUtils.escapeXml(token[i++]), (token.length < 9 ? "" : StringEscapeUtils.escapeXml(token[i++])));
						System.out.println(index + "\t" + tmp);
						writer.write(tmp);
					}
				}
				writer.write("</songs>");
				writer.close();
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Vector<String> listType() {
		Vector<String> types = new Vector<String>();
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("thanhca/the loai.xml")), "UTF-8"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("thanhca/thanhcavietnam.txt")), "UTF-8"));
			String line;
			int index = 0;

			while ((line = reader.readLine()) != null) {
				String[] token = line.split("\t");
				index++;
				if (token.length < 6) {

				} else {
					if (!contain(types, token[6]))
						types.add(token[6]);

				}
			}
			for (int i = 0; i < types.size(); i++)
				writer.write(types.elementAt(i) + "\n");
			writer.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return types;
	}

	public static void getAllAlbum() {
		try {
			ThanhCaDataManager.getInstance().parse("thanhca/thanhcadb.xml");

			Response mRes = Jsoup.connect(URL_MAIN).execute();
			Map<String, String> mCookies = mRes.cookies();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("thanhca/thanhcavietnam.txt"), false), "UTF-8"));
			for (int albumid = 225; albumid < 1236; albumid++) {
				Vector<ThanhCaSongInfo> album = ThanhCaDataManager.getInstance().getAlbum(albumid);
				if (album.size() > 0) {
					for (int i = 0; i < album.size(); i++) {
						ThanhCaSongInfo song = album.get(i);
						String songdetail = "";
						if (!song.getAudio().endsWith(".mp3")) {
							getLogger().info("" + song);
							songdetail = song.getAlbumId() + "\t" + getSongDetail(URL_DETAIL, mCookies, "" + song.getSongId());
						} else {
							songdetail = song.getTextDetail();
						}
						writer.write(songdetail + "\n");
						getLogger().info(songdetail);
						writer.flush();
					}
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getAlbum(String url, Map<String, String> cookies, String albumID) {
		StringBuffer buff = new StringBuffer();
		try {
			Map<String, String> params = new Hashtable<String, String>();
			params.put("url", "Album," + albumID);
			Response albumResponse = null;
			int numTry = 3;
			do {
				albumResponse = getJSoupResponse(url, cookies, params);
				numTry--;
			} while (numTry > 0 && albumResponse == null);

			if (albumResponse != null) {
				Document songDoc = albumResponse.parse();
				Elements songs = songDoc.select("a[href*=Play,]");
				for (Element song : songs) {
					String link = song.attr("abs:href");
					String songID = link.substring(link.indexOf(',') + 1);
					String detail = getSongDetail(URL_DETAIL, cookies, songID);
					if (!detail.trim().isEmpty())
						buff.append(albumID).append("\t").append(detail).append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	public static String getSongDetail(String url, Map<String, String> cookies, String songID) {
		StringBuffer buff = new StringBuffer(songID).append("\t");
		try {
			Map<String, String> params = new Hashtable<String, String>();
			params.put("url", "Play," + songID);
			Response songResponse = null;
			int numTry = 3;
			do {
				songResponse = getJSoupResponse(url, cookies, params);
				numTry--;
			} while (numTry > 0 && songResponse == null);

			if (songResponse != null) {
				Document songDoc = songResponse.parse();
				// System.out.println(songDoc);
				Elements infoTDs = songDoc.select("td[class=fr_2]");

				for (int index = 0; index < 5; index++) {
					Element elm = infoTDs.get(index);
					buff.append(elm.select("b").text()).append("\t");
				}

				Elements download = songDoc.select("a[href*=Download]");

				if (download.size() > 0) {
					String downloadLink = download.get(0).attr("abs:href");
					URLConnection link = null;
					try {
						link = (new URL(downloadLink)).openConnection();
						link.setConnectTimeout(5000);
						link.connect();
						InputStream inp = link.getInputStream();
						buff.append(link.getURL());
						inp.close();
					} catch (Exception e) {
						if(link != null){
							buff.append(link.getURL());
						} else {
							buff.append(downloadLink);
						}
					}
				}
				buff.append("\t");

				Elements downloadpdf = songDoc.select("a[href*=pdf]");
				if (downloadpdf.size() > 0)
					buff.append(downloadpdf.get(0).attr("abs:href"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	public static Response getJSoupResponse(String url, Map<String, String> cookies, Map<String, String> params) {
		try {
			Connection con = Jsoup.connect(url);
			if (params != null && params.size() > 0) {
				Set<String> keys = params.keySet();
				for (String key : keys)
					con = con.data(key, params.get(key));
				// con = con.data(params);
			}

			if (cookies != null && cookies.size() > 0) {
				Set<String> keys = cookies.keySet();
				for (String key : keys)
					con = con.cookie(key, cookies.get(key));
			}

			return con.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:17.0) Gecko/20100101 Firefox/17.0").referrer("http://thanhcavietnam.org/ThanhCaVN/").method(Method.POST).execute();
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean contain(Vector<String> contain, String key) {
		Enumeration<String> fKey = contain.elements();
		while (fKey.hasMoreElements()) {
			String name = fKey.nextElement();
			if (name.equals(key))
				return true;
		}
		return false;
	}

	public static Logger getLogger() {
		return Utils.getLogger();
	}
}
