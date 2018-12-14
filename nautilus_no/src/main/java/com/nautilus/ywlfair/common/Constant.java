package com.nautilus.ywlfair.common;

public class Constant {

    public static String SIGNATURE_SECRET = "780566A57FE0760A7A1E615A7784B41E";

    public static String TALKING_ID = "4e92371e58c94b9f9cc466741df91aee";

    public static final int API_TYPE = CONFIG.FORMAL_API;//正是环境 测试环境切换

    public class URL {

        public static final String APP_CONFIG = "appConfig";

        public static final String TEST_BASE_API = "http://api.yingwuluo.cc/v1.1/";

        public static final String FORMAL_BASE_API = "http://139.196.32.246:8081/v1.1/";

        public static final String NAUTILUS_URL_GET_KUAIDI = "http://m.kuaidi100.com/index_all.html?type=%s&postid=%s";

        public static final String GET_ACCESS_TOKEN = "user/accessToken";

        public static final String DEL_DELETE_ACCESS_TOKEN = "user/%s/accessToken";

        public static final String GET_HOME_PAGER = "newHomepage";

        public static final String GET_RECOMMENDS = "recommendations";

        public static final String POST_USER_QUESTION = "user/%s/question";

        public static final String GET_ACTIVITY_SHARE_INFO = "activity/%s/shareInfo";

        public static final String GET_RECOMMEND_SHARE_INFO = "recommendation/%s/shareInfo";

        public static final String GET_ORIGINAL_SHARE_INFO = "article/%s/shareInfo";

        public static final String GET_USER_STATISTICS = "user/%s/statistics";

        public static final String GET_USER_INFO = "user/%s";

        public static final String GET_USER_RECORDS = "user/%s/activityRecords";

        public static final String GET_USER_TICKETS = "activity/%s/myTicketInfo";

        public static final String GET_USER_BOOTHS = "user/%s/booths";

        public static final String GET_ACTIVITY = "activity/%s";

        public static final String GET_ACTIVITY_PIC = "activity/%s/pictures";

        public static final String GET_COMMENTS_BY_ACTIVITY_ID = "comments";

        public static final String GET_COMMENTS_BY_COMMENT_ID = "comment/%s/comments";

        public static final String GET_COMMENTS_BY_USER_ID = "user/%s/comments";

        public static final String POST_COMMENTS_BY_COMMENT_ID = "comment/%s/comment";

        public static final String POST_COMMENTS_BY_ACTIVITY_ID = "comment";

        public static final String GET_VENDOR_INFO = "vendor/%s";

        public static final String GET_ACTIVITY_VIDEOS = "activity/%s/records";

        public static final String POST_USER_SIGN = "user/%s/sign";

        public static final String POST_RESET_EMAIL = "resetPasswordMail";

        public static final String POST_SET_NEW_PASSWORD = "user/newPassword";

        public static final String GET_USER_MESSAGES = "user/%s/messages";

        public static final String PUT_UPDATE_MESSAGES = "messages/readStatus";

        public static final String GET_USER_ARTICLES = "user/%s/articles";

        public static final String GET_RECOMMEND_ARTICLES = "articles";

        public static final String GET_USER_ORDERS = "user/%s/orders";

        public static final String PUT_ORDER_STATUS = "order/%s/status";

        public static final String POST_LIKE = "like";

        public static final String POST_WANT_JOIN = "activity/%s/participation";

        public static final String DELETE_COMMENT = "comment/%s";

        public static final String POST_BIND_PHONE = "user/%s/bindPhone";

        public static final String GET_SEND_VERIFY_CODE = "verificationCode";

        public static final String POST_CREATE_ORDER = "user/%s/order";

        public static final String POST_VERIFY_CODE = "verificationCode";

        public static final String PUT_PASSWORD = "user/%s/password";

        public static final String POST_REGISTER = "user";

        public static final String GET_ACTIVIES = "activities";

        public static final String GET_COMMENT = "comment/%s";

        public static final String GET_GOODS_HOMEPAGER = "goodsHomepage";

        public static final String GET_GOODS_CLASS = "goodsClassInfos";

        public static final String POST_SHIPPING_ADDERS = "user/%s/address";
        public static final String GET_GOODS_LIST = "goodsList";

        public static final String GET_SHIPPING_ADDERS_LIST = "user/%s/addressList";

        public static final String USER_ID = "user/%s";

        public static final String PAY_PASSWORD = "user/%s/payPassword";

        public static final String ARTICLE_AND_RECOMMEND = "articleAndRecommendations";

        public static final String GET_VALID_ORDER = "order/%s/validOrder";

        public static final String GET_ORDER_DETAIL = "order/%s";

        public static final String GET_GOODS_DETAIL = "goods/%s";

        public static final String GET_ARTICLE_FUN_LIST = "articleFunActivity";

        public static final String GET_VENDOR_DEPOSITS = "vendor/%s/deposits";

        public static final String GET_VENDOR_DEPOSITS_LOG = "vendor/%s/depositsLog";

        public static final String POST_RETURN_DEPOSIT = "vendor/%s/refundApplication";

        public static final String GET_BOOTH_STATUS = "vendor/%s/boothStatus";

        public static final String GET_VENDOR_OFF_LINE_ORDERS = "vendor/%s/offlineOrders";

        public static final String GET_APP_INITIALIZATION = "appInitialization";

        public static final String POST_PUSH_NEXUS = "sysAccsPushNexus";

        public static final String PUSH_TAG = "sysAccsPushTag";

        public static final String GET_OFF_LINE_ORDER = "offlineOrder/%s";

        public static final String GET_ACTIVITY_BOOTH_APPLICATION_CONFIG = "activity/%s/boothApplicationConfig";

        public static final String POST_BOOTH_APPLICATION = "activity/%s/boothApplication";

        public static final String POST_USER_PROBLEM_FEED_BACK = "user/%s/problemFeedback";

        public static final String GET_BOOTH_LIST = "vendor/%s/boothList";

        public static final String GET_BOOTH_DETAIL = "vendor/%s/myBooth";

        public static final String SUBSCRIBE_NOTICE = "subscribe/notice";

        public static final String CREATE_VENDOR = "vendorNew";

        public static final String SING_LIST = "user/%s/signList";

        public static final String GET_ACTIVITY_TICKET_LIST = "activity/%s/ticketList";

        public static final String ACTIVE_TICKETS = "activity/%s/ticketInfo";

        public static final String ACTIVE_STATUS = "activity/%s/newStatusInfo";

        public static final String ACTIVITY_RECORDS_LIST = "user/%s/activityRecordsList";

        public static final String GET_ACTIVITYS_USER = "activitiesUser";

        public static final String GET_ACTIVITYS_VONDER = "activitiesVonder";

        public static final String GET_ACTIVITY_MY_TICKETINFO = "activity/%s/myTicketInfo";

        public static final String GET_USER_NEW_MESSAGES = "user/%s/newMessages";

        public static final String GET_PRIASE_USER_LIST = "likeUserList";

        public static final String GET_TICKET_CODE_STATUS = "ticketCodeStatus";

        public static final String GET_VENDOR_VIP = "vendor/%s/vip";

        public static final String BIND_ALI_PAY = "user/%s/bindPayAccount";

        public static final String CASH_OUT = "user/%s/withdrawAmount";

        public static final String PAY_AUTHORIZATION = "user/%s/payAuthorization";

        public static final String BALANCE_DETAIL = "user/%s/balanceDetailList";

        public static final String GET_WALLET_INFO = "user/%s/wallet";

        public static final String GET_SCORE_LIST = "user/%s/scoreDetailList";

        public static final String GET_BALANCE_DETAIL = "balanceDetail/%s";

        public static final String GET_USER_INVITATION_INFO = "user/%s/invitationInfo";

        public static final String POST_USER_INVITATION_CODE = "user/%s/invitationCode";

        public static final String GET_USER_INVITATION_CODE_INFO = "user/%s/shareInvitationCodeInfo";

        public static final String POST_VALID_INVITATION = "user/validInvitationCode";

        public static final String GET_QR_CODE = "user/%s/offlinePayQrCode";

        public static final String GET_ACCOUNT_STATUS = "user/%s/payAccountStatus";
    }

    public class CONFIG {
        public static final int TEST_API = 0;// 0 = 测试环境 1 = 正是环境

        public static final int FORMAL_API = 1;

    }

    public class KEY {
        public static final String VENDOR_INFO = "vendor_info";

        public static final String CONTEXT_TYPE = "contextType ";

        public static final String INPUT_TXT = "input_txt";

        public static final String HAS_LIKE = "has_like";

        public static final String TICKET_CODE = "ticketCode";

        public static final String VIDEO_PATH = "video_path";

        public static final String ARTICLE = "article";

        public static final String NAUTILUSITEM = "NautilusItem";

        public static final String IS_LIKE = "is_like";

        public static final String CAN_DELETE = "can_delete";

        public static final String COMMENT_TYPE = "comment_type";

        public static final String COMMENT_ID = "comment_id";

        public static final String ID = "id";

        public static final String ITEM_ID = "item_id";

        public static final String IS_MSG = "is_msg";

        public static final String BIND_STATUS = "bind_status";

        public static final String PICINFO_LIST = "picinfo_list";

        public static final String MODE = "mode";

        public static final String EMAIL = "email";

        public static final String PHONE = "phone";

        public static final String PIC_NUM = "pic_num";

        public static final String IS_SET_RESULT = "is_set_result";

        public static final String URIS = "uris";

        public static final String POSITION = "position";

        public static final String IS_USER = "isuser";

        public static final String TYPE = "type";

        public static final String ORDER = "order";

        public static final String ORDER_ID = "order_id";

        public static final String TICKET = "ticket";

        public static final String CLASS_TYPE = "class_type";

        public static final String NUMBER = "number";

        public static final String CODE = "code";

        public static final String SECOND_CLASS = "second_class";

        public static final String GOODS_CATEGORY = "goods_category";

        public static final String GOODS_STYLE = "goods_style";

        public static final String GOODS_STYLE_TAG = "goods_style_tag";

        public static final String GOODS_INFO = "goods_category";

        public static final String NAME = "name";

        public static final String ADDRESS = "address";

        public static final String CHECK_TYPE = "check_type";

        public static final String STALL = "stall";

        public static final String URL = "url";

        public static final String VENDOR_STATUS = "vendor_status";

        public static final String SKU_INFO = "sku_info";

        public static final String DEFAULT_TEXT = "default_text";

        public static final String ITEM_TYPE = "itemType";

        public static final String ORDER_STATUS = "order_status";

        public static final String DEPOSIT = "deposit";

        public static final String BOOTH = "booth";

        public static final String BOOTH_CONFIG = "booth_config";

        public static final String CURRENT_INDEX = "current_index";

        public static final String PRODUCT_KIND = "product_kind";

        public static final String PRODUCT_KIND_ID = "product_kind_id";

        public static final String TWO_CODE_INFO = "two_code_info";

        public static final String ROUND_ID = "round_id";

        public static final String SHARE_INFO = "share_info";

        public static final String COMPANY = "company";

        public static final String WALLET = "wallet";


    }

    public class PRE_KEY {

        public static final String APP_CONFIG = "app_config";

        public static final String ACCESSTOKEN = "accessToken";

        public static final String ACT_MAIN_URL = "act_main_url";

        public static final String USER_NAME = "user_name";

        public static final String PASSWORD = "password";

        public static final String VENDOR_CACHE = "vendor_cache";

        public static final String FIRST_BOOT = "first_boot";

        public static final String FORCE_UPDATE = "force_update";

        public static final String PHONE_NUMBER = "phone_number";

        public static final String USER_TYPE = "user_type";

        public static final String IS_CAN_SEE = "is_can_see";

    }

    public class REQUEST_CODE {
        public static final int GOODS_STYLE = 20000;

        public static final int GOODS_KIND = 20001;

        public static final int RC_SELECT_AVATAR_FROM_ALBUM = 20002;

        public static final int SHOW_PICTURES = 20003;

        public static final int CHECK_USER_INFO = 20004;

        public static final int SELECT_IMAGE = 20005;

        public static final int CROP_IMAGE = 20006;

        public static final int TO_BE_VENDOR = 20007;

        public static final int BIND_PHONE = 20008;

        public static final int CHANGE_NAME = 20009;

        public static final int CHANGE_SIGN = 20010;

        public static final int TO_PAY = 20011;

        public static final int GOODS_DETAIL = 20012;

        public static final int CHANGE_PHONE = 20013;

        public static final int CANCEL_BOOTH = 20014;

        public static final int EDIT_COMMENT = 20015;

        public static final int UP_LOAD_IDENTITY = 20016;

        public static final int EDIT_PRICE = 20017;

        public static final int SET_PASSWORD = 20018;

    }


    public class REQUEST {
        public class KEY {


            public static final String USERID = "userId";
            public static final String VENDOR_ID = "vendorId";

            public static final String PHONE = "phone";


            public static final String WEIXIN = "weixin";
            public static final String STUFFSTYLE = "stuffStyle";
            public static final String STUFFKINDS = "stuffKinds";
            public static final String FOODCERTPICURL = "foodCertPicUrl";
            public static final String FOODELECMSG = "foodElecMsg";
            public static final String SHELFNUM = "shelfNum";
            public static final String INTRMSG = "intrMsg";
            public static final String STUFFPICURL = "stuffPicUrl";
            public static final String LARGEGROUNDURL = "largeGroundUrl";
            public static final String STUFFPRICEAREA = "stuffPriceArea";
            public static final String REALSHOPFLAG = "realshopFlag";
            public static final String LOGOURL = "logoUrl";
            public static final String SPECNEEDS = "specNeeds";
            public static final String MEDIARESMSG = "mediaResMsg";
            public static final String CITYSURVEYMSG = "citySurveyMsg";

            public static final String SCORE = "score";

            public static final String PAY_TYPE = "payType";

            public static final String VENDOR_USER_ID = "vendorUserId";

            public static final String UPDATE_TIME = "time";

            public static final String ACCESS_TOKEN = "accessToken";

            public static final String ACCOUNT = "account";

            public static final String AMOUNT = "amount";

            public static final String ACT_ID = "actId";

            public static final String USER_ACCOUNT = "userAccount";

            public static final String ACCOUNT_NAME = "accountName";

            public static final String PASSWORD = "password";

            public static final String THIRD_PARTY_FLAG = "thirdPartyFlag";

            public static final String NICK_NAME = "nickname";

            public static final String INVITATION_CODE = "invitationCode";

            public static final String START_TIME = "startTime";

            public static final String END_TIME = "endTime";

            public static final String QUERY_TYPE = "queryType";

            public static final String START = "start";

            public static final String BOOTH_TYPE = "boothType";

            public static final String ROWS = "rows";

            public static final String HAS_SHOP = "hasShop";

            public static final String INCLUDE_REPLY = "includeReply";

            public static final String READ_STATUS = "readStatus";

            public static final String AVATAR = "avatar";

            public static final String SEX = "sex";

            public static final String CITY = "city";

            public static final String FEED_BACK_TYPE = "feedbackType";

            public static final String VALID_INVITATION_CODE = "invitationCode";

            public static final String NAME = "name";

            public static final String SIGNATURE = "signature";

            public static final String ACTIVITY_ID = "activityId";

            public static final String TYPE = "type";

            public static final String USER_ID = "userId";

            public static final String CLASS_ID = "classId";

            public static final String LEVEL = "level";

            public static final String CONTENT = "content";

            public static final String ITEM_ID = "itemId";

            public static final String ITEM_TYPE = "itemType";

            public static final String RATING = "rating";

            public static final String PHOTOS = "photos";

            public static final String LOCATION = "location";

            public static final String ONLINE_SHOP_TYPE = "onlineShopType";

            public static final String ONLINE_SHOP_NAME = "onlineShopName";

            public static final String ONLINE_SHOP_ADDRESS = "onlineShopAddress";

            public static final String REAL_SHOP_CITY = "realShopCity";

            public static final String REAL_SHOP_ADDRESS = "realShopAddress";

            public static final String PRODUCT_FROM = "productFrom";

            public static final String PRODUCT_KIND = "productKind";

            public static final String PRODUCT_KIND_NAME = "productKindName";

            public static final String PRODUCT_PRICE = "productPrice";

            public static final String PRODUCT_URL = "productUrl";

            public static final String PRODUCT_BRAND = "productBrand";

            public static final String PRODUCT_LOGO_URL = "productLogUrl";

            public static final String ID_CARD = "IDCard";

            public static final String ID_CARD_URL = "IDCardUrl";

            public static final String SUBMIT_STATUS = "submitStatus";

            public static final String AUTH_TYPE = "authType";

            public static final String COMPANY = "company";

            public static final String INVOICE_TYPE = "invoiceType";

            public static final String JOB_TYPE = "jobType";

            public static final String COORDINATES = "coordinates";

            public static final String SIGN_DESC = "signDesc";

            public static final String EMAIL = "email";

            public static final String BOOTH_ID = "boothId";

            public static final String AUTO_REGISTER = "autoRegisterFlag";

            public static final String CODE = "code";

            public static final String MESSAGE_IDS = "messageIds";

            public static final String ORDER_STATUS = "orderStatus";

            public static final String ORDER_TYPE = "orderType";

            public static final String ORDER_ID = "orderId";

            public static final String IS_WANT_PARTICIPATE = "isWantParticipate";

            public static final String IS_LIKE = "isLike";

            public static final String ROUND_ID = "roundId";

            public static final String CITY_SURVEY_MSG = "citySurveyMsg";

            public static final String SKU_ID = "skuId";

            public static final String ITEM_PRICE = "itemPrice";

            public static final String ITEM_NUM = "itemNum";

            public static final String CHANNEL = "channel";

            public static final String USER_MESSAGE = "userMessage";

            public static final String ADDRESS_ID = "addressId";

            public static final String OLD_PASSWORD = "oldPassword";

            public static final String NEW_PASSWORD = "newPassword";

            public static final String RELATE_ID = "relateId";

            public static final String DEVICE_ID = "deviceId";

            public static final String DEPOSIT_ID = "depositId";

            public static final String TAG = "tag";

            public static final String OBJECT_ID = "objectId";

            public static final String OBJECT_TYPE = "objectType";

            public static final String ALI_PAY_TYPE = "aliPayType";

            public static final String CERTIFICATE_NO = "certificateNo";

            public static final String CERTIFICATE_IMAGE_URL = "certificateImageUrl";

            public static final String PROVINCECODE = "provinceCode";
            public static final String CITYCODE = "cityCode";
            public static final String TELEPHONE = "telephone";
            public static final String ADDRESS = "address";
            public static final String POSTCODE = "postCode";
            public static final String CONSIGNEE = "consignee";
            public static final String DEFAULTFLAG = "defaultFlag";
            public static final String ID = "id";

        }
    }

    public class RESPONSE {
        public class KEY {
            public static final String UPDATE_TIME = "time";
        }

        public class CODE {
            public static final int TOKEN_OUT_DATE = -2;

        }
    }

    public class BROADCAST {
        public static final String LOGIN_BROAD = "login_broad";

        public static final String EXCHANGE_USER = "exchange_user";
    }

}
