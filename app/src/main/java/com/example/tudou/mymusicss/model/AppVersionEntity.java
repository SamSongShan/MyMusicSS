package com.example.tudou.mymusicss.model;

/**
 * APP版本
 */
public class AppVersionEntity {

    /**
     * Code : 200
     * Message : 获取成功
     * Data :
     */

    private int Code;
    private String Message;
    /**
     * ItemName : v1.0.1
     * ItemValue : http://www.baidu.com
     * Description :
     */

    private DataEntity Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public DataEntity getData() {
        return Data;
    }

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public static class DataEntity {
        private String ItemName;
        private String ItemValue;
        private String Description;

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String ItemName) {
            this.ItemName = ItemName;
        }

        public String getItemValue() {
            return ItemValue;
        }

        public void setItemValue(String ItemValue) {
            this.ItemValue = ItemValue;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }
    }
}
