package com.liuk.trade.common.constants;

/**
 * Created by kl on 2018/2/18.
 */
public class TradeEnums {
    public enum RestServerEnum{
        ORDER("localhost",8080,"order"),
        PAY("localhost",8081,"pay"),
        COUPON("localhost",8082,"coupon"),
        GOODS("localhost",8083,"goods"),
        USER("localhost",8084,"user"),
        ;

        private String serverHost;
        private int serverPort;
        private String contextPath;

        RestServerEnum(String serverHost, int serverPort, String contextPath) {
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.contextPath = contextPath;
        }

        public String getServerUrl(){
            return "http://" + this.serverHost + ":" + this.serverPort + "/" + this.contextPath + "/";
        }

        public String getServerHost() {
            return serverHost;
        }

        public int getServerPort() {
            return serverPort;
        }

        public String getContextPath() {
            return contextPath;
        }
    }
    public enum ResEnum{
        SUCCESS("1","成功"),
        FAIL("-1","失败");
        private String code;
        private String desc;

        ResEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
    public enum OrderStatusEnum{
        NO_CONFIRM("0","未确认"),
        CONFIRM("1","已确认"),
        CANCEL("2","已取消"),
        INVALID("3","无效"),
        RETURNED("4","退货");
        private String statusCode;
        private String desc;

        OrderStatusEnum(String statusCode, String desc) {
            this.statusCode = statusCode;
            this.desc = desc;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getDesc() {
            return desc;
        }
    }
    public enum PayStatusEnum{
        NO_PAY("0","未付款"),PAYING("1","支付中"),PAID("2","已支付");
        private String statusCode;
        private String desc;

        PayStatusEnum(String statusCode, String desc) {
            this.statusCode = statusCode;
            this.desc = desc;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getDesc() {
            return desc;
        }
    }
    public enum ShippingStatusEnum{
        NO_SHIP("0","未发货"),SHIPPED("1","已发货"),RECEIVED("2","已收货");
        private String statusCode;
        private String desc;

        ShippingStatusEnum(String statusCode, String desc) {
            this.statusCode = statusCode;
            this.desc = desc;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getDesc() {
            return desc;
        }
    }
    public enum YesNoEnum{
        NO("0","否"),YES("1","是");

        private String code;
        private String desc;

        YesNoEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
    public enum UserMoneyLogTypeEnum{
        PAID("1","订单付款"),REFUND("2","订单退款");

        private String code;
        private String desc;

        UserMoneyLogTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
