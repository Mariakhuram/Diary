package com.mk.diary.utils

import android.net.Uri
import com.mk.diary.domain.models.NoteViewModelClass

object MyConstants {
    val searchList :ArrayList<NoteViewModelClass> by lazy { ArrayList() }
    val list :ArrayList<String> by lazy { ArrayList() }
    val tagList :ArrayList<String> by lazy { ArrayList() }
    var listOfImageCounting = ArrayList<Uri>()
    const val KEY_HAS_SEEN_BOTTOM_SHEET = "has_seen_bottom_sheet"
    const val GRAVITY="GRAVITY"
    const val TOKEN="TOKEN"
    const val FINGERPRINT_TOKEN="FINGERPRINT_TOKEN"
    const val USER_AUTH_TOKEN="USER_AUTH_TOKEN"
    const val ALL_CAPITAL="ALL_CAPITAL"
    const val ALL_SMALL="ALL_SMALL"
    const val ALL_DEFAULT="ALL_DEFAULT"
    const val ALL_NORMAL="ALL_NORMAL"
    const val TEXT_LETTERS="TEXT_LETTERS"
    const val TEXT_SIZE="TEXT_SIZE"
    const val BACK_G_IMAGE="BACK_G_IMAGE"
    const val EMOJI="EMOJI"
    const val FONTS_KEY="FONTS_KEY"
    const val STATUS="STATUS"
    const val TAG_SHAPE="TAG_SHAPE"
    const val PASS_DATA="PASS_DATA"
    const val VOICE="VOICE"
    const val TITLE="TITLE"
    const val TAG_TITLE="TAG_TITLE"
    const val DATE="DATE"
    const val TIME="TIME"
    const val YEAR="YEAR"
    const val MONTH="MONTH"
    const val IMAGE="IMAGE"
    const val DESCRIPTION="DESCRIPTION"
    const val ID="ID"
    const val DELETE="DELETE"
    const val POSITION="POSITION"
    const val URI="URI"
    const val BULLET_STYLE_DIGITS="BULLET_STYLE_DIGITS"
    const val DEFAULT_BULLET_STYLE_DIGITS="DEFAULT_BULLET_STYLE_DIGITS"
    const val BULLET_STYLE_DOTS="BULLET_STYLE_DOTS"
    const val CHECK_FEB_YEAR="CHECK_FEB_YEAR"
    const val CHECK_YEAR="CHECK_FEB_YEAR"
    const val CURRENT_MONTH="CURRENT_MONTH"
    const val CURRENT_CALENDER_TABS_POSITION="CURRENT_CALENDER_TABS_POSITION"
    const val DAY_OF_WEEK="DAY_OF_WEEK"
    const val CURRENT_DATE="CURRENT_DATE"
    const val PASSWORD_FORGOT_KEY="PASSWORD_FORGOT_KEY"
    const val ITEM_VIEW_LIST_TYPE="ITEMV_VIEW_LIST_TYPE"
    const val NOTIFICATION_CLICK="NOTIFICATION_CLICK"
    const val FIRST_TIME_VISIT="FIRST_TIME_VISIT"
    const val APP_THEME_BACKGROUND="APP_THEME_BACKGROUND"
    const val APP_THEME_BACKGROUND_COLOR="APP_THEME_BACKGROUND_COLOR"
    const val LANG="LANG"
    const val FINISH_FRAGMENT="FINISH_FRAGMENT"
    const val USER_TIPS="USER_TIPS"


}