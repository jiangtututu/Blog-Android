package com.example.ayf.wsblogs.unify;

/**
 * Created by asus on 2018/12/16.
 */

public class news_unify {
    /**
     新闻标题
     */
    private String title;
    /**
     * 图片的url
     */
    private String pic;
    /**
     * 新闻具体信息的url，后期用一个webview展示
     */
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }

}
