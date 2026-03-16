/*
    Somnium Reale
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.client.contributor;

public class Contributor {
	public static boolean isLegoTaylor(String uuid) {
		return uuid.equals("772eb47b-a24e-4d43-a685-6ca9e9e132f7") || uuid.equals("3445ebd7-25f8-41a6-8118-0d19d7f5559e");
	}

	public static boolean isContributor(String uuid) {
		return isLegoTaylor(uuid);
	}
}
