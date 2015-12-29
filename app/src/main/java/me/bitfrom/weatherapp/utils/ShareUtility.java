package me.bitfrom.weatherapp.utils;

import android.app.Activity;
import android.content.Intent;

import com.cocosw.bottomsheet.BottomSheet;
import com.cocosw.bottomsheet.BottomSheetHelper;

import me.bitfrom.weatherapp.constants.ConstantsManager;

public class ShareUtility {

    /**
     * Returns bottom sheet builder object, that provides lollipop's like share action via bottom sheet.
     * @param activity activity instance
     * @param dayTemp day temperature
     * @param description general weather description
     * @return
     * **/
    public static BottomSheet.Builder getShareActions(Activity activity, String dateStamp,
                                                       String dayTemp, String description) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dateStamp);
        stringBuilder.append(", ");
        stringBuilder.append(description);
        stringBuilder.append(": ");
        stringBuilder.append(dayTemp);
        stringBuilder.append(". ");
        stringBuilder.append(ConstantsManager.APP_HASHTAG);
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());

        return BottomSheetHelper.shareAction(activity, shareIntent);
    }
}
