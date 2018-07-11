package com.example.tudou.mymusicss.model;

import java.util.List;

/**
 * 通知公告
 */
public class NewsEntity {

    /**
     * Code : 200
     * Message : 获取成功
     * Data :
     */

    private int Code;
    private String Message;
    private List<DataEntity> Data;

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

    public List<DataEntity> getData() {
        return Data;
    }

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public static class DataEntity {
        /**
         * newsid : a2d6c6b3-eaaf-4e7d-b0c9-375f23f094b1
         * category : 版本升级
         * fullhead : V1.0.2升级公告
         */

        private String newsid;
        private String category;
        private String fullhead;
        private String createdate;

        public String getNewsid() {
            return newsid;
        }

        public void setNewsid(String newsid) {
            this.newsid = newsid;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getFullhead() {
            return fullhead;
        }

        public void setFullhead(String fullhead) {
            this.fullhead = fullhead;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }
    }
}
