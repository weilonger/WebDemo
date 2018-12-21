package com.weilong.webdemo.controller;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.weilong.webdemo.entities.Csdn;
import com.weilong.webdemo.entities.HotNews;
import com.weilong.webdemo.entities.Media;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用htmlUnit爬取
 * @author hwl
 */
@Controller
/**
 * 爬虫控制器
 * @author hwl
 */
public class HtmlCrawlController {

    @ResponseBody
    @RequestMapping("/getBaidu")
    /**
     * 获取百度年代热搜
     * @return 返回热搜信息
     */
    public List<Media> getBaidu() throws IOException{
        //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        final WebClient mWebClient = new WebClient(BrowserVersion.CHROME);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        mWebClient.getOptions().setCssEnabled(false);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        mWebClient.getOptions().setThrowExceptionOnScriptError(false);
        //很重要，启用JS
        mWebClient.getOptions().setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        mWebClient.setAjaxController(new NicelyResynchronizingAjaxController());
        final HtmlPage page = mWebClient.getPage("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&ch=7&tn=98012088_9_dg&wd=%E5%B9%B4%E4%BB%A3%E7%83%AD%E9%97%A8%E7%94%B5%E8%A7%86&oq=%25E5%25B9%25B4%25E4%25BB%25A3%25E4%25BA%25BA%25E4%25BB%25AC%25E7%2594%25B5%25E8%25A7%2586&rsv_pq=8e8a00fa00008230&rsv_t=e126XVVEZuDsOTxS9YhT1bGWC0CO5dfOOxoWHM269oZAyOMr4MeNjKrbHZRovRJjJpE3Jg&rqlang=cn&rsv_enter=1&rsv_sug3=8&rsv_sug1=4&rsv_sug7=100&rsv_sug2=0&inputT=991&rsv_sug4=2430&rsv_sug=1");
//        HtmlDivision mdiv = (HtmlDivision)page.getElementById("1");
//        return mdiv.asText();
        DomNodeList<DomNode> ilist = page.querySelectorAll(".op_exactqa_item ");

        List<Media> medias = new ArrayList();

        for (DomNode i:ilist) {
            Media media = new Media();
            HtmlImage image = i.querySelector(".op_exactqa_item_img").querySelector(".c-img");
//            String img = a1.getAttribute("href");
            String img = image.getAttribute("src");
            HtmlAnchor a2 = i.querySelector(".c-gap-top-small").querySelector("a");
            String title = a2.getAttribute("title");

            media.setImg(img);
            media.setTitle(title);
//            media.setEpisodes(episodes);
            medias.add(media);
        }
        return medias;
    }


    @RequestMapping("/getHotMedia")
    @ResponseBody
    /**
     * 获取热门媒资
     * @return 返回热门媒资
     */
    public List<Media> getHotMedia() throws IOException{
        //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        final WebClient mWebClient = new WebClient(BrowserVersion.CHROME);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        mWebClient.getOptions().setCssEnabled(false);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        mWebClient.getOptions().setThrowExceptionOnScriptError(false);
        //很重要，启用JS
        mWebClient.getOptions().setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        mWebClient.setAjaxController(new NicelyResynchronizingAjaxController());
        final HtmlPage page = mWebClient.getPage("http://v.baidu.com/tv/list/order-hot+pn-1+channel-tvplay");
//        bd-filter-content  上方分类div
//        bd-videos 视频div  ->
        DomNode bd_media = page.querySelector(".bd-videos");
        DomNodeList<DomNode> mList = bd_media.querySelectorAll(".bd-video-item");
        List<Media> medias = new ArrayList();
        for (DomNode m: mList) {
            Media media = new Media();
            HtmlImage image = m.querySelector(".bd-video-img");
            String img = image.getAttribute("src");
            HtmlHeading4 h4 = m.querySelector(".bd-video-title");
            String title = h4.getAttribute("title");
            HtmlSpan span = m.querySelector(".bd-video-update");
            String episodes = span.asText();
            media.setImg(img);
            media.setTitle(title);
            media.setEpisodes(episodes);
            medias.add(media);
        }
        return medias;
    }

    @ResponseBody
    @RequestMapping("/getHotNews")
    /**
     * 获取热点新闻
     * @return 返回热点新闻
     */
    public List<HotNews> hotNews() throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        //本次抓取内容的网页，无需css和JavaScript，设为false
        webClient.getOptions().setCssEnabled(false);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        //抓取的网址 http://top.baidu.com/buzz?b=1
        HtmlPage page = webClient.getPage("http://top.baidu.com/buzz?b=26&c=1&fr=topcategory_c1");

        String html = page.asXml();

        Document doc = Jsoup.parse(html);

        Elements as;

        //解析获取拥有list-title的类属性的标签
        as = doc.select(".list-title");
        //处理获取结果
        List<HotNews> news = new ArrayList<>();
        for(Element e:as){
            HotNews n = new HotNews();
            n.setName(e.text());
            n.setUrl(e.attr("href"));
            news.add(n);
        }
        return news;
    }


    //csdn博客爬取
    @RequestMapping("/getBlog")
    @ResponseBody
    /**
     * 抓取csdn博客
     * @return 返回相关博客页面
     */
    public List<Csdn> getBlog(String keyword, @RequestParam(required = false) String pageNo) throws IOException{
        WebClient webClient=new WebClient(BrowserVersion.CHROME);
        //本次抓取内容的网页，无需css和JavaScript，设为false
        webClient.getOptions().setCssEnabled(false);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage page = webClient.getPage("https://so.csdn.net/so/");
        HtmlForm form = page.getFormByName("queryform");
        HtmlTextInput textField = form.getInputByName("q");
        HtmlSubmitInput button = form.getInputByValue("搜一搜");
        textField.setValueAttribute(keyword);
        HtmlPage page2 = button.click();
        String html = page2.asXml();
        Document doc = Jsoup.parse(html);
        Element as = doc.getElementsByClass("search-list-con").first();
        Elements slist = as.getElementsByTag("dl");
        List<Csdn> csdns = new ArrayList<>();
        for(Element s:slist){
            Csdn csdn = new Csdn();
            Element a1 = s.getElementsByTag("a").first();
            String name = a1.text();
            String url = a1.attr("href");
            HtmlPage page3 = webClient.getPage(url);
            csdn.setName(name);
            csdn.setUrl(url);
            boolean isEmpty = s.getAllElements().hasClass("search-detail");
            if (isEmpty){
                Element dd1 = s.getElementsByClass("search-detail").first();
                String detail = dd1.text();
                csdn.setDetail(detail);
            }
            csdns.add(csdn);
        }
        return csdns;
    }

    @RequestMapping(value = "/searchBlog")
    public List<Csdn> searchBlog(){

        return null;
    }

}
