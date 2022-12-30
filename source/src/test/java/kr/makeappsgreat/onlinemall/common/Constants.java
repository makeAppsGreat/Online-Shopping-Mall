package kr.makeappsgreat.onlinemall.common;

import org.h2.api.ErrorCode;

public class Constants {

    public static String H2_DUPLICATE_KEY = String.format("[%d-%d]", ErrorCode.DUPLICATE_KEY_1, org.h2.engine.Constants.BUILD_ID);
}
