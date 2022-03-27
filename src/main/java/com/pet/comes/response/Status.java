package com.pet.comes.response;

import org.springframework.stereotype.Component;

@Component
public class Status {

    /*
     * 200 success
     * 3XX input error
     * 4XX server error
     * 5XX database error
     */

    public static int SUCCESS = 200;
    public static int ILLEGAL_ARGUMENT = 400;
    public static int ILLEGAL_STATE = 401;

    public int DUPLICATED_EMAIL = 301;
    public int NOT_ENTERED = 302;
    public int DUPLICATED_NICKNAME = 303;
    public int INVALID_ID = 304;
    public int EXISTED_NICKNAME = 305;
    public int INVALID_DOGID = 306;
    public int INVALID_FAMILYID = 307;
    public int LIST_IS_EMPTY = 308;
    public int EMPTY_VALUE = 309;
    public int TOO_LONG_VALUE = 310;
    public int INVALID_NO = 311;
    public int NOTHING = 312;
    public int NOT_ACHIEVED = 313;
    public int TAKEN_BADGE = 314;
    public int INVALID_BADGE = 315;
    public int FAMILY_UNREGISTERED = 316;
    public int PIN_UNREGISTERED = 317;
    public int BADGE_ACHIEVEMENT_CONDITION_UNREGISTERED = 318;
    public int DIARY_UNREGISTERED = 318;
    public int COMMENT_UNREGISTERED = 319;
    public int SCHEDULE_UNREGISTERED = 320;
    public int INVALID_DATE = 321;
    public int INVALID_TIME = 322;
    public int INVALID_VALUE = 323;
    public int NO_PERMISSION = 324;
    public int INVALID_ITEM = 325;

    public int DB_INVALID_VALUE = 500;
    public int DB_NO_DATA = 501;
    public int EXPIRED_TOKEN = 502;

}
