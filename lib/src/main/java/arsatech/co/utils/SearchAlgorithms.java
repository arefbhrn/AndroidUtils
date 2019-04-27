package arsatech.co.utils;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class SearchAlgorithms {

	private static String normalize(String str) {
		return StringUtils.strToPersian(str.toLowerCase());
	}

	public static class InStrings {

		public static ArrayList<String> startingWithSearch(ArrayList<String> list, String query) {
			query = normalize(query);
			ArrayList<String> result = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				String temp = normalize(list.get(i));
				;
				if (temp.startsWith(query)) {
					result.add(list.get(i));
				}
			}
			return result;
		}

		public static ArrayList<String> exactSearch(ArrayList<String> list, String query) {
			query = normalize(query);
			ArrayList<String> result = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				String temp = normalize(list.get(i));
				;
				if (temp.equals(query)) {
					result.add(list.get(i));
				}
			}
			return result;
		}

		public static ArrayList<String> containSearch(ArrayList<String> list, String query) {
			query = normalize(query);
			ArrayList<String> result = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				int lastCharIndex;
				String temp = normalize(list.get(i));
				;
				boolean isOK = true;
				for (int j = 0; j < query.length(); j++) {
					if (!temp.contains((query.charAt(j) + ""))) {
						isOK = false;
						break;
					}
					lastCharIndex = temp.indexOf(query.charAt(j));
					if (temp.length() > lastCharIndex + 1) {
						temp = temp.substring(lastCharIndex + 1);
					}
				}
				if (isOK) {
					result.add(list.get(i));
				}
			}
			return result;
		}

		public static ArrayList<String> exactContainSearch(ArrayList<String> list, String query) {
			query = normalize(query);
			ArrayList<String> result = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				String temp = normalize(list.get(i));
				;
				if (temp.contains(query)) {
					result.add(temp);
				}
			}
			return result;
		}

		public static ArrayList<String> faultyContainSearch(ArrayList<String> list, String query) {
			query = normalize(query);
			ArrayList<Pair<String, Float>> result = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				String temp = normalize(list.get(i));
				;
				if (temp.charAt(0) != query.charAt(0)) {
					continue;
				}
				float score = 1;
				int lastCharIndex;
				for (int j = 1; j < query.length(); j++) {
					if (!temp.contains(query.charAt(j) + "")) {
						continue;
					}
					score++;
					lastCharIndex = temp.indexOf(query.charAt(j));
					if (temp.length() > lastCharIndex + 1) {
						temp = temp.substring(lastCharIndex + 1);
					}
				}
				score /= query.length();
				result.add(new Pair<>(list.get(i), score));
			}
			Collections.sort(result, (o1, o2) -> {
				if (o1.second < o2.second) {
					return 1;
				} else if (o1.second > o2.second) {
					return -1;
				}
				return 0;
			});
			ArrayList<String> finalResult = new ArrayList<>();
			for (int i = 0; i < result.size(); i++) {
				if (result.get(i).second > 0.4) {
					finalResult.add(result.get(i).first);
				}
			}
			return finalResult;
		}

	}

}
