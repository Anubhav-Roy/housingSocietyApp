package roy.anubhav.core.utils;

import android.content.Intent;

/**
 * Helpers to start activities in a modularized world.
 *
 * Uses Deep-Linking to start different activities .
 */

public class ActivityHelper {

    private static final String PACKAGE_NAME= "roy.anubhav.home";
    private static final String CLASS_NAME= "roy.anubhav";

    /**
     * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
     */
    public static Intent intentTo(String className) {

        return new Intent(Intent.ACTION_VIEW).setClassName(
                PACKAGE_NAME,
                CLASS_NAME+"."+className);
    }

}
