/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.xhl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.PngImage;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawMap {

	// static BufferedWriter out;
	public static void main(String[] args) {
		// Input parameter from command line
		// action 1 for map with charging station, 2 for event 2 with
		// distribution, 3 for road status
		int action = Integer.parseInt(args[0]);
		// level 1 to 6 of road types
		int level = Integer.parseInt(args[1]);
		// Type of Color Set
		int colorType = Integer.parseInt(args[2]);
		// Alpha Value
		int alpha = Integer.parseInt(args[3]);
		// Charging station file name
		String stationFileName = args[4];
		// Open Street Map XML file
		String openMapName = args[5];
		// PDF / PNG file name
		String outputFileName = args[6];

		// Testing
		// action 1 for map with charging station, 2 for event 2 with
		// distribution, 3 for road status
		// int action = 3;
		// level 1 to 6 of road types
		// int level = 5;
		// Type of Color Set
		// int colorType = 2;
		// Alpha Value
		// int alpha = 220;
		// Charging station file name
		// String stationFileName =
		// "D:\\Education\\Huawei\\Map\\EVStation.info";
		// Heat map for seekin event
		// String stationFileName =
		// "D:\\Education\\Huawei\\Matrix\\Task7_HeatMap_Integrated_0005_NZ.txt";
		// Open Street Map XML file
		// String openMapName = "D:\\Education\\Huawei\\Map\\map";
		// PDF / PNG file name
		// String outputFileName =
		// "D:\\Education\\Huawei\\Map\\Shenzhen_Custom_" + level;

		// Output file name
		String pngFileName = outputFileName + ".png";
		String pdfFileName = outputFileName + ".pdf";

		/*
		 * For road analysis try { out = new BufferedWriter(new
		 * FileWriter("D:\\Education\\Huawei\\Map\\Road_Distance_" + level +
		 * ".txt")); } catch (IOException ex) { ex.printStackTrace(); }
		 */
		DrawMap sm = new DrawMap();

		// Read <node> element
		String[] nodes = sm.readnodes(openMapName);
		// Read <way> element
		String[] ways = sm.readways(openMapName);
		// Combine <node> and <way>
		String[] waysLatLon = sm.combine(nodes, ways);
		// Each way information
		String[] waynodes = sm.wayonlynodes(nodes, ways);
		String minmaxlatlon = sm.mmll(waynodes);

		// The area of map (min lantitude, max lantitude, min longitude, max
		// longitude)
		// minmaxlatlon = "22.447202 22.693849 113.769264 114.329910"; //
		// Shenzhen area
		// minmaxlatlon = "22.447202 22.697849 113.769264 114.329910"; //
		// Shenzhen area
		minmaxlatlon = "22.447202 22.707849 113.769264 114.329910"; // Shenzhen
																	// area
		System.out.println(">>" + minmaxlatlon);

		int width = 3000;
		sm.drawimage(width, minmaxlatlon, waysLatLon, level, stationFileName, pngFileName, pdfFileName, action,
				colorType, alpha);
				/*
				 * For Testing for (int i = 0; i < waysLatLon.length; i++) {
				 * System.out.println(waysLatLon[i]); }
				 */

		/*
		 * For road analysis try { out.close(); } catch (IOException ex) {
		 * ex.printStackTrace(); }
		 */
	}

	private Color[] getColorSet(int type, int alpha) {
		Color[] set = null;
		switch (type) {
		case 1:
			// Google set
			set = new Color[5];
			set[0] = new Color(169, 169, 169, alpha);
			set[1] = new Color(0, 255, 0, alpha);
			set[2] = new Color(255, 165, 0, alpha);
			set[3] = new Color(255, 0, 0, alpha);
			set[4] = new Color(128, 0, 0, alpha);
			break;
		case 2:
			// Custom set
			set = new Color[5];
			set[0] = new Color(169, 169, 169, alpha);
			set[1] = new Color(65, 105, 225, alpha);
			set[2] = new Color(0, 0, 205, alpha);
			set[3] = new Color(0, 0, 128, alpha);
			set[4] = new Color(25, 25, 112, alpha);
			break;
		}
		return set;
	}

	private String[] getExplanationSet(int type) {
		String[] set = null;
		switch (type) {
		case 1:
			// Range set
			set = new String[5];
			// set[0] = "Count = 0";
			// set[1] = "1 <= Count <= 40";
			// set[2] = "41 <= Count <= 80 ";
			// set[3] = "81 <= Count <= 120";
			// set[4] = "Count => 121";
			set[0] = "= 0";
			set[1] = "[1,10]";
			set[2] = "[11,30] ";
			set[3] = "[31,60]";
			set[4] = ">= 61";
			break;
		}
		return set;
	}

	private String[] wayonlynodes(String[] nodes, String[] ways) {
		String[] waysLatLon = new String[ways.length];

		int count = 0;
		for (int k = 0; k < ways.length; k++) {
			String[] tokens = ways[k].split(" ");
			count = count + tokens.length - 2;
			// System.out.println(ways[k]+" "+count);
		}

		String[] waynodes = new String[count];
		int j = 0;
		for (int k = 0; k < ways.length; k++) {
			String[] tokens = ways[k].split(" ");

			for (int r = 1; r < tokens.length - 1; r++) {
				String nodeID = tokens[r];
				String latlon = position(nodeID, nodes);

				waynodes[j] = latlon;
				// System.out.println("waynodes[" + j + "] : " + waynodes[j]);
				j++;
			}
		}

		return waynodes;
	}

	private BufferedImage rotate90ToLeft(BufferedImage inputImage) {
		// The most of code is same as before
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		BufferedImage returnImage = new BufferedImage(height, width, inputImage.getType());
		// We have to change the width and height because when you rotate the
		// image by 90 degree, the
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				returnImage.setRGB(y, width - x - 1, inputImage.getRGB(x, y));
				// Again check the Picture for better understanding
			}
		}
		return returnImage;

	}

	private BufferedImage addExplanation(BufferedImage inputImage, int width, int height, int colorType, int alpha) {
		int cap = BasicStroke.CAP_ROUND;
		int join = BasicStroke.JOIN_ROUND;
		int x = width - 800;
		int y = height - 800;
		Graphics2D ig2 = inputImage.createGraphics();
		ig2.setPaint(Color.BLACK);
		ig2.setStroke(new BasicStroke(15));
		ig2.drawRect(x, y, 600, 700);
		Color[] set = this.getColorSet(colorType, alpha);
		String[] explain = this.getExplanationSet(1);
		for (int i = 0; i < set.length; i++) {
			ig2.setPaint(set[i]);
			ig2.setStroke(new BasicStroke(32, cap, join));
			ig2.drawLine(x + 50, y + 125, x + 300, y + 125);
			ig2.setPaint(Color.black);
			ig2.setFont(new Font("TimesRoman", Font.BOLD, 60));
			ig2.drawString(explain[i], x + 350, y + 125);
			y += 125;
		}
		return inputImage;
	}

	private void pngToPdf(String iFileName, String pFileName, int width, int height) {
		try {
			String imageFileName = iFileName; // Image File name
			String pdfFileName = pFileName; // PDF file name

			// Create Document Object
			Document convertPngToPdf = new Document();
			convertPngToPdf.setPageSize(new com.itextpdf.text.Rectangle(width + 100, height + 100));
			// Create PdfWriter for Document to hold physical file
			// Change the PDF file path to suit to your needs
			PdfWriter.getInstance(convertPngToPdf, new FileOutputStream(pdfFileName));
			convertPngToPdf.open();
			// Get the PNG image to Convert to PDF
			// getImage of PngImage class is a static method
			// Edit the file location to suit to your needs
			com.itextpdf.text.Image convertBmp = PngImage.getImage(imageFileName);
			// Add image to Document
			convertPngToPdf.add(convertBmp);
			// Close Document
			convertPngToPdf.close();
			System.out.println("Converted and stamped PNG Image in a PDF Document Using iText and Java");
		} catch (Exception i1) {
			i1.printStackTrace();
		}
	}

	private void drawRoadStatus(double minlat, double minlon, double maxlat, double maxlon, int width, int height,
			ArrayList<String> ways, String dFileName, Graphics2D ig2, int colorType, int alpha) {
		String way[];
		String temp[];
		String line;
		// Stroke setting
		int cap = BasicStroke.CAP_ROUND;
		int join = BasicStroke.JOIN_ROUND;
		// Latitude and longitude of seeking event
		double cBlat1;
		double cBlon1;
		double cBlat2;
		double cBlon2;
		// Actual latitude and longitude of way
		double dlat1;
		double dlon1;
		double dlat2;
		double dlon2;
		// Identify the bottom left and top right
		double bottomlan;
		double bottomlon;
		double toplan;
		double toplon;
		// X,Y of the image
		int idlat1;
		int idlon1;
		int idlat2;
		int idlon2;
		// Colors
		Color[] set = this.getColorSet(colorType, alpha);
		// Buffer area of cell
		double buffer;
		double length = 0.00005;
		try {
			for (int i = 0; i < ways.size(); i++) {
				way = ways.get(i).split(",");
				if (way[0].equals("1")) { // v="motorway_link"
					ig2.setStroke(new BasicStroke(32, cap, join));
					buffer = 0.0016;
				} else if (way[0].equals("2")) {// v="trunk_link"
					ig2.setStroke(new BasicStroke(20, cap, join));
					buffer = 0.0010;
				} else if (way[0].equals("3")) { // v="primary_link"
					ig2.setStroke(new BasicStroke(8, cap, join));
					buffer = 0.004;
				} else if (way[0].equals("4")) { // v="secondary_link"
					ig2.setStroke(new BasicStroke(6, cap, join));
					buffer = 0.003;
				} else if (way[0].equals("5")) { // v="tertiary_link"
					ig2.setStroke(new BasicStroke(4, cap, join));
					buffer = 0.002;
				} else { // unclassified
					ig2.setStroke(new BasicStroke(2, cap, join));
					buffer = 0.001;
				}
				dlat1 = Double.parseDouble(way[1]);
				dlon1 = Double.parseDouble(way[2]);
				dlat2 = Double.parseDouble(way[3]);
				dlon2 = Double.parseDouble(way[4]);

				// Find the point bottom left and top right
				if (dlat1 < dlat2) {
					bottomlan = dlat1;
					toplan = dlat2;
				} else {
					bottomlan = dlat2;
					toplan = dlat1;
				}
				if (dlon1 < dlon2) {
					bottomlon = dlon1;
					toplon = dlon2;
				} else {
					bottomlon = dlon2;
					toplon = dlon1;
				}

				/*
				 * For road analysis out.write(way[0] + "," + Math.sqrt(((toplan
				 * - bottomlan) * (toplan - bottomlan)) + ((toplon - bottomlon)
				 * * (toplon - bottomlon)))); out.newLine();
				 */
				BufferedReader in = new BufferedReader(new FileReader(dFileName));
				int count = 0;
				boolean inside = false;
				while ((line = in.readLine()) != null) {
					temp = line.split(",");
					cBlat1 = Double.parseDouble(temp[1]);
					cBlon1 = Double.parseDouble(temp[2]);
					cBlat2 = Double.parseDouble(temp[3]);
					cBlon2 = Double.parseDouble(temp[4]);
					// cut the cell into many small cells
					for (double j = cBlat1; j <= cBlat2; j += length) {
						for (double k = cBlon1; k <= cBlon2; k += length) {
							if (this.isInsideTheCell(bottomlan - buffer, bottomlon - buffer, toplan + buffer,
									toplon + buffer, j - (length / 2), k + (length / 2))) {
								count++;
								inside = true;
								break;
							}
						}

						if (inside == true) {
							break;
						}

					}
				}

				if (count == 0) {
					ig2.setPaint(set[0]);
				} else if (count >= 1 && count <= 10) {
					ig2.setPaint(set[1]);
				} else if (count >= 11 && count <= 30) {
					ig2.setPaint(set[2]);
				} else if (count >= 31 && count <= 60) {
					ig2.setPaint(set[3]);
				} else {
					ig2.setPaint(set[4]);
				}
				idlat1 = (int) ((double) width / (maxlat - minlat) * (dlat1 - minlat));
				idlon1 = (int) ((double) height / (maxlon - minlon) * (dlon1 - minlon));
				idlat2 = (int) ((double) width / (maxlat - minlat) * (dlat2 - minlat));
				idlon2 = (int) ((double) height / (maxlon - minlon) * (dlon2 - minlon));
				ig2.drawLine(idlat1, idlon1, idlat2, idlon2);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private boolean isInsideTheCell(double bLan, double bLon, double tLan, double tLon, double rLan, double rLon) {
		boolean inside = false;
		if ((rLan >= bLan && rLan <= tLan) && (rLon >= bLon && rLon <= tLon)) {
			inside = true;
		}
		return inside;
	}

	private void drawRecordDistribution(double minlat, double minlon, double maxlat, double maxlon, int width,
			int height, String dFileName, Graphics2D ig2, int colorType, int alpha) {
		String line;
		String[] temp;
		double cBlat1;
		double cBlon1;
		double cBlat2;
		double cBlon2;
		double fLat;
		double fLon;
		int cAdlat2;
		int cAdlon2;
		int cAdlatlen;
		int cAdlonlen;
		int heatCount;
		double scale = 0.0005;
		cAdlatlen = (int) (Math.ceil((double) width / (maxlat - minlat) * (scale)));
		cAdlonlen = (int) (Math.ceil((double) height / (maxlon - minlon) * (scale)));
		// System.out.println("Length :" + cAdlatlen + " / " + cAdlonlen);
		try {
			BufferedReader in = new BufferedReader(new FileReader(dFileName));
			while ((line = in.readLine()) != null) {
				temp = line.split(",");
				cBlat1 = Double.parseDouble(temp[1]);
				cBlon1 = Double.parseDouble(temp[2]);
				cBlat2 = Double.parseDouble(temp[3]);
				cBlon2 = Double.parseDouble(temp[4]);
				heatCount = Integer.parseInt(temp[5]);
				fLat = cBlat2;
				fLon = cBlon1;
				cAdlat2 = (int) ((double) width / (maxlat - minlat) * (fLat - minlat));
				cAdlon2 = (int) ((double) height / (maxlon - minlon) * (fLon - minlon));
				// cAdlatlen = (int) ((double) width / (maxlat - minlat) *
				// (cBlat2 - cBlat1));
				// cAdlonlen = (int) ((double) height / (maxlon - minlon) *
				// (cBlon2 - cBlon1));
				// alpha value 0-255

				if (heatCount == 0) {
					continue;
				} else if (heatCount >= 1 && heatCount <= 1) {
					ig2.setPaint(new Color(65, 105, 225, alpha));
				} else if (heatCount >= 2 && heatCount <= 3) {
					ig2.setPaint(new Color(0, 0, 205, alpha));
				} else if (heatCount >= 4 && heatCount <= 5) {
					ig2.setPaint(new Color(0, 0, 128, alpha));
				} else {
					ig2.setPaint(new Color(25, 25, 112, alpha));
				}
				ig2.setStroke(new BasicStroke(0));
				ig2.fillRect(cAdlat2, cAdlon2, cAdlatlen, cAdlonlen);
			}
			in.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void drawChargingStation(double minlat, double minlon, double maxlat, double maxlon, int width, int height,
			String sFileName, Graphics2D ig2) {

		String line;
		String[] temp;
		double cBlat2;
		double cBlon2;
		int cAdlat2;
		int cAdlon2;

		try {
			BufferedReader in = new BufferedReader(new FileReader(sFileName));
			while ((line = in.readLine()) != null) {
				temp = line.split(" ");
				cBlat2 = Double.parseDouble(temp[3]);
				cBlon2 = Double.parseDouble(temp[4]);
				cAdlat2 = (int) ((double) width / (maxlat - minlat) * (cBlat2 - minlat));
				cAdlon2 = (int) ((double) height / (maxlon - minlon) * (cBlon2 - minlon));

				ig2.setPaint(Color.blue); // color of circle
				ig2.setStroke(new BasicStroke(15)); // width of line
				ig2.drawOval(cAdlat2, cAdlon2, 70, 70); // draw the circle
				ig2.fillOval(cAdlat2 + 15, cAdlon2 + 15, 40, 40); // draw the
																	// dot
																	// inside
			}
			in.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void drawimage(int w, String minmaxlatlon, String[] waysLatLon, int level, String sFileName,
			String pngFileName, String pdfFileName, int action, int colorType, int alpha) {
		try {
			ArrayList<String> ways = new ArrayList<String>();
			String[] tokensminmax = minmaxlatlon.split(" ");
			double minlat = Double.parseDouble(tokensminmax[0]);
			double maxlat = Double.parseDouble(tokensminmax[1]);
			double minlon = Double.parseDouble(tokensminmax[2]);
			double maxlon = Double.parseDouble(tokensminmax[3]);
			System.out.println(
					"minlat : " + minlat + ", minlon : " + minlon + ", maxlat : " + maxlat + ", maxlon : " + maxlon);
			int width = w;
			int height = (int) (w * (maxlon - minlon) / (maxlat - minlat));
			System.out.println("width : " + width + ", height : " + height);
			// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
			// into integer pixels
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			Graphics2D ig2 = bi.createGraphics();
			ig2.setPaint(Color.black);

			double lat1 = 0.0;
			double lon1 = 0.0;
			double lat2 = 0.0;
			double lon2 = 0.0;

			int dlat1 = 0;
			int dlon1 = 0;
			int dlat2 = 0;
			int dlon2 = 0;

			int wType = 0;
			int cap = BasicStroke.CAP_ROUND;
			int join = BasicStroke.JOIN_ROUND;
			ig2.setBackground(Color.white);
			for (int i = 0; i < waysLatLon.length; i++) {
				String[] tokens = waysLatLon[i].split(" ");
				// System.out.println("--"+waysLatLon[i]);
				if (tokens[tokens.length - 1].equals("motorway")) { // v="motorway_link"
					ig2.setStroke(new BasicStroke(32, cap, join));
					wType = 1;
					if (action == 1) {
						ig2.setPaint(new Color(105, 105, 105, alpha));
					} else {
						ig2.setPaint(new Color(169, 169, 169, alpha));
					}
				} else if (tokens[tokens.length - 1].equals("trunk")) {// v="trunk_link"
					if (level < 2) {
						continue;
					}
					wType = 2;
					ig2.setStroke(new BasicStroke(20, cap, join));
					if (action == 1) {
						ig2.setPaint(new Color(128, 128, 128, alpha));
					} else {
						ig2.setPaint(new Color(169, 169, 169, alpha));
					}
				} else if (tokens[tokens.length - 1].equals("primary")) { // v="primary_link"
					if (level < 3) {
						continue;
					}
					wType = 3;
					ig2.setStroke(new BasicStroke(8, cap, join));
					if (action == 1) {
						ig2.setPaint(new Color(112, 128, 144, alpha));
					} else {
						ig2.setPaint(new Color(169, 169, 169, alpha));
					}
				} else if (tokens[tokens.length - 1].equals("secondary")) { // v="secondary_link"
					if (level < 4) {
						continue;
					}
					wType = 4;
					ig2.setStroke(new BasicStroke(6, cap, join));
					if (action == 1) {
						ig2.setPaint(new Color(119, 136, 153, alpha));
					} else {
						ig2.setPaint(new Color(169, 169, 169, alpha));
					}
				} else if (tokens[tokens.length - 1].equals("tertiary")) { // v="tertiary_link"
					if (level < 5) {
						continue;
					}
					wType = 5;
					ig2.setStroke(new BasicStroke(4, cap, join));
					if (action == 1) {
						ig2.setPaint(new Color(169, 169, 169, alpha));
					} else {
						ig2.setPaint(new Color(169, 169, 169, alpha));
					}
				} else { // unclassified
					if (level < 6) {
						continue;
					}
					wType = 6;
					ig2.setStroke(new BasicStroke(2, cap, join));
					if (action == 1) {
						ig2.setPaint(new Color(192, 192, 192, alpha));
					} else {
						ig2.setPaint(new Color(169, 169, 169, alpha));
					}
				}

				for (int j = 1; j < tokens.length - 6; j = j + 3) {
					lat1 = Double.parseDouble(tokens[j + 1]);
					lon1 = Double.parseDouble(tokens[j + 2]);
					lat2 = Double.parseDouble(tokens[j + 4]);
					lon2 = Double.parseDouble(tokens[j + 5]);

					// System.out.println("Before : " + lat1 + " " + lon1 + " "
					// + lat2 + " " + lon2);
					dlat1 = (int) ((double) width / (maxlat - minlat) * (lat1 - minlat));
					dlon1 = (int) ((double) height / (maxlon - minlon) * (lon1 - minlon));
					dlat2 = (int) ((double) width / (maxlat - minlat) * (lat2 - minlat));
					dlon2 = (int) ((double) height / (maxlon - minlon) * (lon2 - minlon));

					if (action == 3) {
						ways.add(wType + "," + lat1 + "," + lon1 + "," + lat2 + "," + lon2);
					} else {
						ig2.drawLine(dlat1, dlon1, dlat2, dlon2);
					}
				}
			}

			if (action == 1) {
				this.drawChargingStation(minlat, minlon, maxlat, maxlon, width, height, sFileName, ig2);
			} else if (action == 2) {
				this.drawRecordDistribution(minlat, minlon, maxlat, maxlon, width, height, sFileName, ig2, colorType,
						alpha);
			} else if (action == 3) {
				this.drawRoadStatus(minlat, minlon, maxlat, maxlon, width, height, ways, sFileName, ig2, colorType,
						alpha);
			} else {
				// Nothing
			}
			ig2.setPaint(Color.BLACK);
			ig2.setStroke(new BasicStroke(15));
			ig2.drawRect(0, 0, width, height);
			bi = this.rotate90ToLeft(bi);
			// add the explanation frame
			bi = this.addExplanation(bi, height, width, colorType, alpha);
			// Generate PNG
			ImageIO.write(bi, "PNG", new File(pngFileName));
			// Convert PNG to PDF
			this.pngToPdf(pngFileName, pdfFileName, height, width);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	private String mmll(String[] nodes) {
		double minlat = 10000000.0;
		double maxlat = -1000000.0;
		double minlon = 10000000.0;
		double maxlon = -1000000.0;
		for (int i = 0; i < nodes.length; i++) {
			String[] tokens = nodes[i].split(" ");

			if (minlat > Double.parseDouble(tokens[1])) {
				minlat = Double.parseDouble(tokens[1]);
			}
			if (maxlat < Double.parseDouble(tokens[1])) {
				maxlat = Double.parseDouble(tokens[1]);
			}
			if (minlon > Double.parseDouble(tokens[2])) {
				minlon = Double.parseDouble(tokens[2]);
			}
			if (maxlon < Double.parseDouble(tokens[2])) {
				maxlon = Double.parseDouble(tokens[2]);
			}
		}
		return minlat + " " + maxlat + " " + minlon + " " + maxlon;
	}

	private String[] combine(String[] nodes, String[] ways) {
		String[] waysLatLon = new String[ways.length];

		for (int k = 0; k < ways.length; k++) {
			String[] tokens = ways[k].split(" ");
			waysLatLon[k] = tokens[0];

			// Ways[k] - Way id, node ref, tag v arttribute
			// System.out.println("Ways[k] : " + ways[k]);
			for (int r = 1; r < tokens.length - 1; r++) {
				String nodeID = tokens[r];

				String latlon = position(nodeID, nodes);

				waysLatLon[k] = waysLatLon[k] + " " + latlon;
			}
			// Ways[k] - Way id, [node ref, , lat, lon], v arttribute
			waysLatLon[k] = waysLatLon[k] + " " + tokens[tokens.length - 1];
			// System.out.println("waysLatLon[k] : " + waysLatLon[k]);
		}
		return waysLatLon;
	}

	private String position(String nodeID, String[] nodes) {
		int start = 0;
		int end = nodes.length;
		int middle = start + (end - start) / 2;

		// System.out.println("nodeID="+nodes[middle]);
		long nodeid = Long.parseLong(nodeID);
		String[] tokens = nodes[middle].split(" ");
		long id = Long.parseLong(tokens[0]);
		while (true) {
			if (nodeid == id) {
				break;
			}
			if (nodeid > id) {
				start = middle;
				end = end;
				middle = start + (end - start) / 2;
				tokens = nodes[middle].split(" ");
				id = Long.parseLong(tokens[0]);
			}
			if (nodeid < id) {
				start = start;
				end = middle;
				middle = start + (end - start) / 2;
				tokens = nodes[middle].split(" ");
				id = Long.parseLong(tokens[0]);
			}
		}
		return nodes[middle];
	}

	private String[] readnodes(String filename) {
		int count = 0;

		// Count the number of nodes
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("node id=")) {
					count++;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] nodes = new String[count];
		count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("node id=")) {
					// Node element
					// System.out.println(sCurrentLine);
					String[] tokens = sCurrentLine.split(" ");
					String[] tokensID = tokens[3].split("\"");
					String[] tokensLAT = tokens[4].split("\"");
					String[] tokensLON = tokens[5].split("\"");

					// node id
					// System.out.println(tokens[3]);
					// latitude
					// System.out.println(tokens[4]);
					// longitude
					// System.out.println(tokens[5]);
					// node ID, latitute, Longitude
					nodes[count] = tokensID[1] + " " + tokensLAT[1] + " " + tokensLON[1];
					count++;
				}

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// for (int i = 0; i < nodes.length; i++) {
		/* For checking node information */
		// System.out.println(i + " -- " + nodes[i]);
		// }
		return nodes;
	}

	private String[] readways(String filename) {
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			// count the number of highway
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("way id=")) {
					while ((sCurrentLine = br.readLine()) != null) {
						if (sCurrentLine.contains("/way")) {
							break;
						}
						if (sCurrentLine.contains("highway")) {
							count++;
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(count);
		String[] nodes = new String[count];
		int count1 = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if (count1 >= count) {
					break;
				}
				if (sCurrentLine.contains("way id=")) {
					String[] tokens = sCurrentLine.split(" ");
					// way element
					// System.out.println(sCurrentLine);
					// id=xxxxxxx
					// System.out.println(tokens[3]);
					String[] tokensID = tokens[3].split("\"");
					nodes[count1] = tokensID[1];

					while ((sCurrentLine = br.readLine()) != null) {
						// end tag of way element
						if (sCurrentLine.contains("/way")) {
							// System.out.println("\n\n");
							break;
						}
						// System.out.println(sCurrentLine);
						if (sCurrentLine.contains("nd ref=")) {
							String[] tokensPID = sCurrentLine.split("\"");
							// System.out.println(sCurrentLine);
							// ref id
							// System.out.println(">> "+tokensPID[1]);
							nodes[count1] = nodes[count1] + " " + tokensPID[1];
						}
						if (sCurrentLine.contains("highway")) {
							String[] tokensHG = sCurrentLine.split("\"");
							// highway type
							// System.out.println(">> "+tokensHG[3]);
							nodes[count1] = nodes[count1] + " " + tokensHG[3];
							count1++;
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * for (int i = 0; i < nodes.length; i++) { System.out.println(i +
		 * " -- " + nodes[i]); }
		 */
		return nodes;
	}
}
