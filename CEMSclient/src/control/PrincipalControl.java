package control;

import java.util.ArrayList;

import common.ExtraTimeRequest;

public class PrincipalControl {
	public static ArrayList<ExtraTimeRequest> requests;
	public static int ifRequests;
	public static int HowManyExamsNow;

	private static boolean isExtraTimeRequest_Recived = false;

	public static boolean isExtraTimeRequest_Recived() {
		return isExtraTimeRequest_Recived;
	}

	public static void setExtraTimeRequest_Recived(boolean isExtraTimeRequest_Recived) {
		PrincipalControl.isExtraTimeRequest_Recived = isExtraTimeRequest_Recived;
	}

}