package com.nautilus.ywlfair.common.utils.voley;

import android.text.TextUtils;

import com.android.volley.Request;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.BoothApplicationInfo;
import com.nautilus.ywlfair.entity.bean.VendorInfo;

import java.util.Map;
import java.util.TreeMap;

import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.ORDER_STATUS;
import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.ROWS;
import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.START;

public class RequestUtil {

    public static String getInterfaceUrl(String interfaceName, String param) {

        return null;
    }

    public static Map<String, String> getAccessTokenParams(String account, String password, int thirdPartyFlag, String nickname) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ACCOUNT, account);

        params.put(Constant.REQUEST.KEY.PASSWORD, password);

        params.put(Constant.REQUEST.KEY.THIRD_PARTY_FLAG, String.valueOf(thirdPartyFlag));

        params.put(Constant.REQUEST.KEY.NICK_NAME, nickname);

        return params;
    }

    public static Map<String, String> getInvitationInfoParams(String invitationCode) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.INVITATION_CODE, invitationCode);

        return params;
    }

//    String type, String readStatus, String start, String rows

    public static Map<String, String> getUserNewMessagesParams(String type, String readStatus, String start, String rows) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put(START, start);
        params.put(ROWS, rows);
        params.put(Constant.REQUEST.KEY.TYPE, type);
        params.put(Constant.REQUEST.KEY.READ_STATUS, readStatus);


        return params;
    }

    public static Map<String, String> PutTicketCodeStatusParams(String codeStatus) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.KEY.TICKET_CODE, codeStatus);


        return params;
    }

    public static Map<String, String> GetStallOrderInfoListParams(String orderStatus, String start, String rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(START, start);

        params.put(ORDER_STATUS, orderStatus);

        params.put(ROWS, rows);

        return params;
    }

    public static Map<String, String> getRecommendParams(int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        return params;
    }

    public static Map<String, String> getPraiseUserParams(String itemId, String itemType, int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ITEM_ID, itemId);

        params.put(Constant.REQUEST.KEY.ITEM_TYPE, itemType);

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        return params;
    }

    public static Map<String, String> getMyTicketsDetailParams(String orderId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ORDER_ID, orderId);

        return params;
    }

    public static Map<String, String> getOrderDetailParams(String orderType) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ORDER_TYPE, orderType);

        return params;
    }

    public static Map<String, String> getMyTicketsListParams(String type, String start, String rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.TYPE, type);

        params.put(ROWS, rows);

        params.put(START, start);

        return params;
    }

    public static Map<String, String> getSignListParams(String start, String rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(ROWS, rows);

        params.put(START, start);

        return params;
    }

    public static Map<String, String> getUserOrdersParams(String orderStatus, int start, int rows, String orderType) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ORDER_STATUS, String.valueOf(orderStatus));

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        params.put(Constant.REQUEST.KEY.ORDER_TYPE, orderType);


        return params;
    }

    public static Map<String, String> getPutUserInfoParams(String avatar, String nickName,
                                                           String sex, String city, String signature) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.AVATAR, avatar);

        params.put(Constant.REQUEST.KEY.NICK_NAME, nickName);

        params.put(Constant.REQUEST.KEY.SEX, sex);

        params.put(Constant.REQUEST.KEY.CITY, city);

        params.put(Constant.REQUEST.KEY.SIGNATURE, signature);

        return params;
    }

    public static Map<String, String> getUserBoothsParams(String orderId) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put(Constant.REQUEST.KEY.ORDER_ID, orderId);
        return params;
    }

    public static Map<String, String> getGetMySiginParams(String activityId) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put(Constant.REQUEST.KEY.ACTIVITY_ID, activityId);
        return params;
    }

    public static Map<String, String> getActivePictureParams(int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        return params;
    }

    public static Map<String, String> getBalanceDetailListParams(String startTime, String endTime, int queryType, int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.START_TIME, startTime);

        params.put(Constant.REQUEST.KEY.END_TIME, endTime);

        params.put(Constant.REQUEST.KEY.QUERY_TYPE, queryType + "");

        params.put(Constant.REQUEST.KEY.START, start + "");

        params.put(Constant.REQUEST.KEY.ROWS, rows + "");

        return params;
    }

    public static Map<String, String> getScoreListParams(String startTime, String endTime, int queryType, int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.START_TIME, startTime);

        params.put(Constant.REQUEST.KEY.END_TIME, endTime);

        params.put(Constant.REQUEST.KEY.QUERY_TYPE, queryType + "");

        params.put(Constant.REQUEST.KEY.START, start + "");

        params.put(Constant.REQUEST.KEY.ROWS, rows + "");

        return params;
    }

    public static Map<String, String> getInitializationParams(String deviceId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.DEVICE_ID, deviceId);

        return params;
    }

    public static Map<String, String> getDepositRecordDetailsParams(String depositId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.DEPOSIT_ID, depositId);

        return params;
    }

    public static Map<String, String> getGoodsListParams(String classId, int level, int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.CLASS_ID, classId);

        params.put(Constant.REQUEST.KEY.LEVEL, String.valueOf(level));

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        return params;
    }

    public static Map<String, String> getBoothListParams(int type, int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.TYPE, String.valueOf(type));

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        return params;
    }

    public static Map<String, String> getBoothDetailParams(String orderId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ORDER_ID, orderId);

        return params;
    }

    public static Map<String, String> getCancelParams(String orderId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ORDER_ID, orderId);

        return params;
    }

    public static Map<String, String> getCommentListParams(String itemId, String itemType, int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        params.put(Constant.REQUEST.KEY.ITEM_ID, itemId);

        params.put(Constant.REQUEST.KEY.ITEM_TYPE, itemType);

        return params;
    }

    public static Map<String, String> getCommentReplyParams(int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        params.put(Constant.REQUEST.KEY.INCLUDE_REPLY, "1");

        return params;
    }

    public static Map<String, String> getUserMessagesParams(int type, int readStatus, int start, int rows) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.TYPE, String.valueOf(type));

        params.put(Constant.REQUEST.KEY.READ_STATUS, String.valueOf(readStatus));

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        return params;
    }

    public static Map<String, String> getPutMessagesParams(String messageIds, int type) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.READ_STATUS, String.valueOf(type));

        params.put(Constant.REQUEST.KEY.MESSAGE_IDS, messageIds);

        return params;
    }

    public static Map<String, String> getPutPasswordParams(String oldPassword, String newPassword) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.OLD_PASSWORD, oldPassword);

        params.put(Constant.REQUEST.KEY.NEW_PASSWORD, newPassword);

        return params;
    }

    public static Map<String, String> getBoothNoticeParams(String userId, String objectId, int objectType) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.USER_ID, userId);

        params.put(Constant.REQUEST.KEY.OBJECT_ID, objectId);

        params.put(Constant.REQUEST.KEY.OBJECT_TYPE, String.valueOf(objectType));

        return params;
    }

    public static Map<String, String> getPutOrderParams(String orderStatus, String type) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ORDER_STATUS, orderStatus);

        params.put(Constant.REQUEST.KEY.ORDER_TYPE, String.valueOf(type));

        return params;
    }

    public static Map<String, String> getUserCommentParams(int start, int rows, int type) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(START, String.valueOf(start));

        params.put(ROWS, String.valueOf(rows));

        params.put(Constant.REQUEST.KEY.TYPE, String.valueOf(type));

        return params;
    }

    public static Map<String, String> getPostCommentParams(String userId, String content) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.USER_ID, userId);

        params.put(Constant.REQUEST.KEY.CONTENT, content);

        return params;
    }

    public static Map<String, String> getPostCommentByActParams(
            String userId, String content, String itemId,
            String itemType, String rating, String photos, String location,
            String coordinates) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.USER_ID, userId);

        params.put(Constant.REQUEST.KEY.CONTENT, content);

        params.put(Constant.REQUEST.KEY.ITEM_ID, itemId);

        params.put(Constant.REQUEST.KEY.ITEM_TYPE, itemType);

        params.put(Constant.REQUEST.KEY.RATING, rating);

        params.put(Constant.REQUEST.KEY.PHOTOS, photos);

        params.put(Constant.REQUEST.KEY.LOCATION, location);

        params.put(Constant.REQUEST.KEY.COORDINATES, coordinates);

        return params;
    }

    public static Map<String, String> getPostSignParams(String signDesc, String location,
                                                        String coordinates, String activityId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.SIGN_DESC, signDesc);

        params.put(Constant.REQUEST.KEY.LOCATION, location);

        params.put(Constant.REQUEST.KEY.COORDINATES, coordinates);

        params.put(Constant.REQUEST.KEY.ACTIVITY_ID, activityId);

        return params;
    }

    public static Map<String, String> getResetEmailParams(String email) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.EMAIL, email);

        return params;
    }

    public static Map<String, String> getResetPasswordParams(String phone, String psw) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PHONE, phone);

        params.put(Constant.REQUEST.KEY.PASSWORD, psw);

        return params;
    }

    public static Map<String, String> getPayAuthorizationParams(String password) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PASSWORD, password);

        return params;
    }

    public static Map<String, String> getSetPayPasswordParams(String psw) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PASSWORD, psw);

        return params;
    }

    public static Map<String, String> getCashOutParams(String amount, String password) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PASSWORD, password);

        params.put(Constant.REQUEST.KEY.AMOUNT, amount);

        return params;
    }

    public static Map<String, String> getShippingAddress(String consignee, String provinceCode, String cityCode,
                                                         String telephone, String address, String postCode) {
//        Name, provinceCode, cityCode, telephone, postCode, address
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PROVINCECODE, provinceCode);
        params.put(Constant.REQUEST.KEY.CITYCODE, cityCode);
        params.put(Constant.REQUEST.KEY.TELEPHONE, telephone);
        params.put(Constant.REQUEST.KEY.ADDRESS, address);
        params.put(Constant.REQUEST.KEY.POSTCODE, postCode);
        params.put(Constant.REQUEST.KEY.CONSIGNEE, consignee);


        return params;
    }

    public static Map<String, String> getWantJoinParams(int isWantParticipate, String userId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.IS_WANT_PARTICIPATE, String.valueOf(isWantParticipate));

        params.put(Constant.REQUEST.KEY.USER_ID, userId);

        return params;
    }

    public static Map<String, String> getVerifyCodeParams(String phone, String code, int autoRegisterFlag) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PHONE, phone);

        params.put(Constant.REQUEST.KEY.CODE, code);

        params.put(Constant.REQUEST.KEY.AUTO_REGISTER, autoRegisterFlag + "");

        return params;
    }

    public static Map<String, String> getRegisterParams(String email, String password,
                                                        String nickname, String phone, int thirdPartyFlag, String relateId,String invitationCode) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.EMAIL, email);

        params.put(Constant.REQUEST.KEY.PASSWORD, password);

        params.put(Constant.REQUEST.KEY.NICK_NAME, nickname);

        params.put(Constant.REQUEST.KEY.PHONE, phone);

        params.put(Constant.REQUEST.KEY.THIRD_PARTY_FLAG, String.valueOf(thirdPartyFlag));

        params.put(Constant.REQUEST.KEY.RELATE_ID, relateId);

        params.put(Constant.REQUEST.KEY.INVITATION_CODE, invitationCode);


        return params;
    }

    public static Map<String, String> getSendCodeParams(String phone, int type) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PHONE, phone);

        params.put(Constant.REQUEST.KEY.TYPE, String.valueOf(type));

        return params;
    }

    public static Map<String, String> PostProblemFeedbackParams(String feedbackType, String phone, String content, String vendorUserId) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put(Constant.REQUEST.KEY.FEED_BACK_TYPE, feedbackType);

        params.put(Constant.REQUEST.KEY.PHONE, phone);

        params.put(Constant.REQUEST.KEY.CONTENT, content);

        params.put(Constant.REQUEST.KEY.VENDOR_USER_ID, vendorUserId);


        return params;
    }

    public static Map<String, String> PostValidInvatationParams(String validInvitationCode) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.VALID_INVITATION_CODE, validInvitationCode);

        return params;
    }

    public static Map<String, String> PostUserQuestionParams(String actId, String content) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put(Constant.REQUEST.KEY.ACT_ID, actId);

        params.put(Constant.REQUEST.KEY.CONTENT, content);

        return params;
    }

    public static Map<String, String> getBindPhoneParams(String phone, String code) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.PHONE, phone);

        params.put(Constant.REQUEST.KEY.CODE, code);

        return params;
    }

    public static Map<String, String> getBoothApplication(BoothApplicationInfo boot) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put(Constant.REQUEST.KEY.PHONE, boot.getPhone());
        params.put(Constant.KEY.NAME, boot.getName());
        params.put(Constant.REQUEST.KEY.USERID, boot.getUserId());
        params.put(Constant.REQUEST.KEY.WEIXIN, boot.getWeixin());
        params.put(Constant.REQUEST.KEY.STUFFSTYLE, boot.getStuffStyle());
        params.put(Constant.REQUEST.KEY.STUFFKINDS, boot.getStuffKinds());
        params.put(Constant.REQUEST.KEY.FOODCERTPICURL, boot.getFoodCertPicUrl());
        params.put(Constant.REQUEST.KEY.FOODELECMSG, boot.getFoodElecMsg());
        params.put(Constant.REQUEST.KEY.SHELFNUM, boot.getShelfNum());
        params.put(Constant.REQUEST.KEY.INTRMSG, boot.getIntrMsg());
        params.put(Constant.REQUEST.KEY.STUFFPICURL, boot.getStuffPicUrl());
        params.put(Constant.REQUEST.KEY.LARGEGROUNDURL, boot.getLargeGroundUrl());
        params.put(Constant.REQUEST.KEY.STUFFPRICEAREA, boot.getStuffPriceArea());
        params.put(Constant.REQUEST.KEY.REALSHOPFLAG, boot.getRealshopFlag());
        params.put(Constant.REQUEST.KEY.LOGOURL, boot.getLogoUrl());
        params.put(Constant.REQUEST.KEY.SPECNEEDS, boot.getSpecNeeds());
        params.put(Constant.REQUEST.KEY.MEDIARESMSG, boot.getMediaResMsg());
        params.put(Constant.REQUEST.KEY.ROUND_ID, boot.getRoundId());
        params.put(Constant.REQUEST.KEY.CITY_SURVEY_MSG, boot.getCitySurveyMsg() + "");
        params.put("boothBuyType", boot.getBoothBuyType());
        params.put("citySurveyMsgText", boot.getCitySurveyMsgText());
        params.put("stuffPriceAreaText", boot.getStuffPriceAreaText());
        params.put("realshopFlagText", boot.getRealshopFlagText());
        params.put("stuffStyleText", boot.getStuffStyleText());
        params.put("stuffKindsText", boot.getStuffKindsText());

        return params;
    }

    public static Map<String, String> getCreateVendorParams(boolean isAdd, VendorInfo vendorInfo) {

        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.USER_ID, vendorInfo.getUserId() + "");

        params.put(Constant.REQUEST.KEY.AUTH_TYPE, vendorInfo.getAuthType() + "");

        if (!TextUtils.isEmpty(vendorInfo.getCompany()))
            params.put(Constant.REQUEST.KEY.COMPANY, vendorInfo.getCompany() + "");

        params.put(Constant.REQUEST.KEY.INVOICE_TYPE, vendorInfo.getInvoiceType() + "");

        params.put(Constant.REQUEST.KEY.JOB_TYPE, vendorInfo.getJobType() + "");

        if (!isAdd) {

            params.put(Constant.REQUEST.KEY.NAME, vendorInfo.getName());

            params.put(Constant.REQUEST.KEY.PHONE, vendorInfo.getPhone());

            params.put(Constant.REQUEST.KEY.ADDRESS, vendorInfo.getAddress());

            params.put(Constant.REQUEST.KEY.HAS_SHOP, vendorInfo.getHasShop() + "");

            if (vendorInfo.getHasShop() == 1) {
                params.put(Constant.REQUEST.KEY.ONLINE_SHOP_TYPE, vendorInfo.getOnlineShopType() + "");

                params.put(Constant.REQUEST.KEY.ONLINE_SHOP_NAME, vendorInfo.getOnlineShopName() + "");

                params.put(Constant.REQUEST.KEY.ONLINE_SHOP_ADDRESS, vendorInfo.getOnLineShopAddress() + "");

                params.put(Constant.REQUEST.KEY.REAL_SHOP_CITY, vendorInfo.getRealShopCity() + "");

                params.put(Constant.REQUEST.KEY.REAL_SHOP_ADDRESS, vendorInfo.getRealShopAddress());
            }

            params.put(Constant.REQUEST.KEY.PRODUCT_FROM, vendorInfo.getProductFrom() + "");

            params.put(Constant.REQUEST.KEY.PRODUCT_KIND, vendorInfo.getProductKind() + "");

            params.put(Constant.REQUEST.KEY.PRODUCT_KIND_NAME, vendorInfo.getProductKindName() + "");

            params.put(Constant.REQUEST.KEY.PRODUCT_PRICE, vendorInfo.getProductPrice() + "");

            params.put(Constant.REQUEST.KEY.PRODUCT_URL, vendorInfo.getProductUrl() + "");

            params.put(Constant.REQUEST.KEY.PRODUCT_BRAND, vendorInfo.getProductBrand() + "");

            params.put(Constant.REQUEST.KEY.PRODUCT_LOGO_URL, vendorInfo.getProductLogUrl() + "");

            params.put(Constant.REQUEST.KEY.ID_CARD, vendorInfo.getIdCard());

            params.put(Constant.REQUEST.KEY.ID_CARD_URL, vendorInfo.getIdCardUrlString());

            params.put(Constant.REQUEST.KEY.SUBMIT_STATUS, "1");
        }

        return params;
    }

    public static Map<String, String> getCreateOrderParams(
            String itemType, String skuId, String itemPrice, String itemNum,
            String phone, String channel, String userMessage, String addressId, String orderNo, String boothId,
            String vendorUserId, String payType, String score) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.VENDOR_USER_ID, vendorUserId);

        params.put(Constant.REQUEST.KEY.PAY_TYPE, payType);

        if(!TextUtils.isEmpty(score))
            params.put(Constant.REQUEST.KEY.SCORE, score);

        params.put(Constant.REQUEST.KEY.ITEM_TYPE, itemType);

        params.put(Constant.REQUEST.KEY.SKU_ID, skuId);

        params.put(Constant.REQUEST.KEY.ITEM_PRICE, itemPrice);

        params.put(Constant.REQUEST.KEY.ITEM_NUM, itemNum);

        if(!TextUtils.isEmpty(phone))
            params.put(Constant.REQUEST.KEY.PHONE, phone);

        params.put(Constant.REQUEST.KEY.CHANNEL, channel);

        params.put(Constant.REQUEST.KEY.USER_MESSAGE, userMessage);

        if(!TextUtils.isEmpty(addressId))
            params.put(Constant.REQUEST.KEY.ADDRESS_ID, addressId);

        params.put(Constant.REQUEST.KEY.ORDER_ID, orderNo);

        params.put(Constant.REQUEST.KEY.BOOTH_ID, boothId);

        return params;
    }

    public static Map<String, String> getLikeParams(String activeId, int itemType, int isLike, int userId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ITEM_ID, String.valueOf(activeId));

        params.put(Constant.REQUEST.KEY.ITEM_TYPE, String.valueOf(itemType));

        params.put(Constant.REQUEST.KEY.IS_LIKE, String.valueOf(isLike));

        if (userId != 0) {
            params.put("userId", String.valueOf(userId));
        }

        return params;
    }

    public static Map<String, String> getActiveStatusParams(String type, String userId, String roundId) {

        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.TYPE, type);

        params.put(Constant.REQUEST.KEY.USER_ID, userId);

        if (!TextUtils.isEmpty(roundId)) {
            params.put(Constant.REQUEST.KEY.ROUND_ID, roundId);
        }

        return params;
    }

    public static Map<String, String> getValidOrderParams(String channel) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.CHANNEL, channel);

        return params;
    }

    public static Map<String, String> getBoothApplyInit(String roundId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ROUND_ID, roundId);

        return params;
    }

    public static Map<String, String> getReturnDepositParams(String depositId) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.DEPOSIT_ID, depositId);

        return params;
    }

    public static Map<String, String> getPostPushNexusParams(String deviceId, int userId, String nickname) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.DEVICE_ID, deviceId);

        params.put(Constant.REQUEST.KEY.USER_ID, String.valueOf(userId));

        params.put(Constant.REQUEST.KEY.NICK_NAME, nickname);

        if (userId != -1)
            params.put(Constant.REQUEST.KEY.USER_ACCOUNT, String.valueOf(userId));

        return params;
    }

    public static Map<String, String> getPostPushTagParams(int userId, String tag) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.USER_ID, String.valueOf(userId));

        params.put(Constant.REQUEST.KEY.TAG, tag);

        return params;
    }

    public static Map<String, String> getBindAliPayParams(int aliPayType, String account, String name,
                                                          String certificateNo, String certificateImageUrl) {
        TreeMap<String, String> params = new TreeMap<>();

        params.put(Constant.REQUEST.KEY.ALI_PAY_TYPE, String.valueOf(aliPayType));

        params.put(Constant.REQUEST.KEY.ACCOUNT, account);

        params.put(Constant.REQUEST.KEY.NAME, name);

        params.put(Constant.REQUEST.KEY.CERTIFICATE_NO, certificateNo);

        params.put(Constant.REQUEST.KEY.CERTIFICATE_IMAGE_URL, certificateImageUrl);

        return params;
    }
}
